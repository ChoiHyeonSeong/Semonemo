package com.semonemo.presentation.screen.auction

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.domain.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuctionProcessViewModel
    @Inject
    constructor(
        private val auctionRepository: AuctionRepository,
        private val saveStateHandle: SavedStateHandle,
    ) : ViewModel() {
        var auctionBidLog = mutableStateOf<List<AuctionBidLog>>(listOf())
            private set
        var topPrice = mutableIntStateOf(0)
            private set
        var auctionId = saveStateHandle["auctionId"] ?: -1L
            private set
        private val _uiEvent = MutableSharedFlow<AuctionUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            joinAuction()
        }

        private fun joinAuction() {
            if (auctionId == -1L) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionUiEvent.Error("경매가 종료되었습니다."))
                }
                return
            }
            viewModelScope.launch {
                auctionRepository.joinAuction(auctionId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            auctionBidLog.value = response.data
                        }
                    }
                }
            }
        }

        private fun updateBidLog(bidLog: AuctionBidLog)  {
            auctionBidLog.value += bidLog
        }

        private fun exitBidLog(bidLog: AuctionBidLog)  {
            auctionBidLog.value -= bidLog
        }
    }
