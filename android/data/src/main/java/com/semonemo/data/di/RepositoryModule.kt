package com.semonemo.data.di

import com.semonemo.data.datasource.AuthDataSourceImpl
import com.semonemo.data.datasource.TokenDataSourceImpl
import com.semonemo.data.repository.AiRepositoryImpl
import com.semonemo.data.repository.AuthRepositoryImpl
import com.semonemo.data.repository.NFTRepositoryImpl
import com.semonemo.data.repository.UserRepositoryImpl
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.datasource.TokenDataSource
import com.semonemo.domain.repository.AiRepository
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    fun bindNFTRepository(nftRepositoryImpl: NFTRepositoryImpl): NFTRepository

    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun bindApiRepository(aiRepositoryImpl: AiRepositoryImpl): AiRepository

    @Binds
    fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    fun bindAuthDataSource(authDataSourceImpl: AuthDataSourceImpl): AuthDataSource

    @Binds
    fun bindTokenDataSource(tokenDataSourceImpl: TokenDataSourceImpl): TokenDataSource
}
