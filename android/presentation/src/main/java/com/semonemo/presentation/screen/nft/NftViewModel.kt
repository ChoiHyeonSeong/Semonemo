package com.semonemo.presentation.screen.nft

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.Transaction
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.request.TransferRequest
import com.semonemo.presentation.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.Dapp
import io.metamask.androidsdk.ErrorType
import io.metamask.androidsdk.Ethereum
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.EthereumState
import io.metamask.androidsdk.RequestError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.FunctionReturnDecoder
import org.web3j.abi.TypeReference
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import javax.inject.Inject

@HiltViewModel
class NftViewModel
    @Inject
    constructor(
        private val ethereum: Ethereum,
        private val dApp: Dapp,
        private val nftRepository: NFTRepository,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        private val web3j =
            Web3j.build(HttpService("https://rpc.ssafy-blockchain.com"))
        private val walletAddress = mutableStateOf("")
        private val transactionState =
            MutableStateFlow<Transaction>(Transaction("", "", "", "", "", ""))
        private val _nftEvent = MutableSharedFlow<NftEvent>()
        val nftEvent = _nftEvent.asSharedFlow()
        val ethereumState =
            MediatorLiveData<EthereumState>().apply {
                addSource(ethereum.ethereumState) { newEthereumState ->
                    value = newEthereumState
                }
            }

        init {
            loadWalletAddress()
        }

        private fun loadWalletAddress() {
            viewModelScope.launch {
                walletAddress.value = authDataSource.getWalletAddress().toString()
            }
        }

        fun connect(callback: ((String) -> Unit)) {
            ethereum.connect(dApp) { result ->
                if (result is RequestError) {
                    callback(result.message)
                } else {
                    walletAddress.value = ethereum.selectedAddress
                    callback(ethereum.selectedAddress)
                }
            }
        }

        private fun addEthereumChain(
            chainId: String,
            chainName: String,
            rpcUrls: String,
            onError: (message: String) -> Unit,
        ) {
            val addChainParams: Map<String, Any> =
                mapOf(
                    "chainId" to chainId,
                    "chainName" to chainName,
                    "rpcUrls" to listOf(rpcUrls),
                )
            val addChainRequest =
                EthereumRequest(
                    method = EthereumMethod.ADD_ETHEREUM_CHAIN.value,
                    params = listOf(addChainParams),
                )

            ethereum.sendRequest(addChainRequest) { result ->
                if (result is RequestError) {
                    onError("Add chain error: ${result.message}")
                }
            }
        }

        fun switchChain(
            chainId: String,
            chainName: String,
            rpcUrls: String,
            onError: (message: String, action: (() -> Unit)?) -> Unit,
            onSuccess: (String) -> Unit,
        ) {
            val hexChainId = "0x" + chainId.toLong().toString(16)
            val switchChainParams: Map<String, String> = mapOf("chainId" to hexChainId)
            val switchChainRequest =
                EthereumRequest(
                    method = EthereumMethod.SWITCH_ETHEREUM_CHAIN.value,
                    params = listOf(switchChainParams),
                )

            ethereum.sendRequest(switchChainRequest) { result ->
                if (result is RequestError) {
                    if (result.code == ErrorType.UNRECOGNIZED_CHAIN_ID.code || result.code == ErrorType.SERVER_ERROR.code) {
                        val message = "${chainId}에 해당되는 체인이 없어서 추가해야함."
                        val action: () -> Unit = {
                            addEthereumChain(
                                hexChainId,
                                chainName,
                                rpcUrls,
                                onError = { error -> onError(error, null) },
                            )
                        }
                        onError(message, action)
                    } else {
                        onError("Switch 성공 에러", null)
                    }
                } else {
                    onSuccess(result.toString())
                }
            }
        }

        fun transfer(
            toAddress: String,
            amount: String,
        ) {
            viewModelScope.launch {
                nftRepository
                    .transfer(
                        TransferRequest(
                            fromAddress = walletAddress.value,
                            toAddress = toAddress,
                            amount = amount,
                        ),
                    ).collectLatest {
                        it?.let { transaction ->
                            transactionState.value = transaction
                        }
                    }
            }
        }

        fun sendTransaction(data: String) {
            val params: MutableMap<String, Any> =
                mutableMapOf(
                    "from" to walletAddress.value,
                    "to" to BuildConfig.CONTRACT_ADDRESS,
                    "data" to data,
                )

            val transactionRequest =
                EthereumRequest(
                    method = EthereumMethod.ETH_SEND_TRANSACTION.value,
                    params = listOf(params),
                )
            ethereum.run { sendRequest(transactionRequest) }
        }

        fun getUserBalance(
            onSuccess: (String) -> Unit,
            onError: (String) -> Unit,
        ) {
            viewModelScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        val function =
                            Function(
                                "getUserBalance",
                                listOf(Address(walletAddress.value)),
                                listOf<TypeReference<*>>(object : TypeReference<Uint256>() {}),
                            )

                        val encodedFunction = FunctionEncoder.encode(function)
                        val contractAddress = "0x503932fFA68504646FebC302aedFEBd7f64CcAd8"

                        val response =
                            web3j
                                .ethCall(
                                    org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(
                                        walletAddress.value,
                                        contractAddress,
                                        encodedFunction,
                                    ),
                                    DefaultBlockParameterName.LATEST,
                                ).send()
                        if (response.hasError()) {
                            throw Exception("ETH calling Error: ${response.error.message}")
                        }
                        val returnValues =
                            FunctionReturnDecoder.decode(response.value, function.outputParameters)
                        val decodedValue = returnValues[0]
                        onSuccess(decodedValue.value.toString())
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown error")
                }
            }
        }
    }
