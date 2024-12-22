package com.jefisu.auth.di

import com.jefisu.auth.data.AuthRepositoryImpl
import com.jefisu.auth.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun provideAuthRepository(repository: AuthRepositoryImpl): AuthRepository
}