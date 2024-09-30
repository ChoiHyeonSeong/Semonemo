package com.semonemo.data.repository

import com.google.gson.Gson
import com.semonemo.data.network.api.NFTApi
import com.semonemo.data.util.toRequestBody
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.IpfsResponse
import com.semonemo.domain.model.Transaction
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.request.TransferRequest
import com.semonemo.domain.request.UploadFrameRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class NFTRepositoryImpl
    @Inject
    constructor(
        private val api: NFTApi,
    ) : NFTRepository {
        override suspend fun transfer(request: TransferRequest): Flow<Transaction?> =
            flow {
                runCatching {
                    api.transfer(request)
                }.onSuccess {
                    emit(it)
                }.onFailure {
                    emit(null)
                }
            }

        override suspend fun uploadImage(image: File): Flow<ApiResponse<IpfsResponse>> =
            flow {
                val requestFile = image.asRequestBody("image/*".toMediaTypeOrNull())
                val file = MultipartBody.Part.createFormData("file", image.name, requestFile)
                runCatching {
                    api.uploadImage(file)
                }.onSuccess {
                    it.body()?.let { ipfs ->
                        emit(ApiResponse.Success(ipfs))
                    }
                }.onFailure {
                    emit(
                        ApiResponse.Error.ServerError(
                            errorMessage = it.message ?: "",
                            errorCode = it.cause.toString(),
                        ),
                    )
                }
            }

        override suspend fun uploadFrame(request: UploadFrameRequest): Flow<ApiResponse<IpfsResponse>> =
            flow {
                val jsonString = Gson().toJson(request)
                val requestBody = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
                val file = MultipartBody.Part.createFormData("file", "frame", requestBody)
                runCatching { api.uploadFrame(file) }
                    .onSuccess {
                        it.body()?.let { ipfs ->
                            emit(ApiResponse.Success(ipfs))
                        }
                    }.onFailure {
                        emit(
                            ApiResponse.Error.ServerError(
                                errorMessage = it.message ?: "",
                                errorCode = it.cause.toString(),
                            ),
                        )
                    }
            }

        override suspend fun test(str: String): Flow<ApiResponse<IpfsResponse>> =
            flow {
                val requestBody = str.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                val file = MultipartBody.Part.createFormData("file", str, requestBody)
                runCatching {
                    api.uploadImage(file)
                }.onSuccess {
                    it.body()?.let { ipfs ->
                        emit(ApiResponse.Success(ipfs))
                    }
                }.onFailure {
                    emit(
                        ApiResponse.Error.ServerError(
                            errorMessage = it.message ?: "",
                            errorCode = it.cause.toString(),
                        ),
                    )
                }
            }
    }
