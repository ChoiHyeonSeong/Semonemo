package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.request.SignUpRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AuthRepository {
    suspend fun exists(address: String): Flow<ApiResponse<Boolean>>

    suspend fun validateNickname(nickname: String): Flow<ApiResponse<Boolean>>

    suspend fun signUp(
        data: SignUpRequest,
        profileImage: File,
    ): Flow<ApiResponse<Unit>>
}
