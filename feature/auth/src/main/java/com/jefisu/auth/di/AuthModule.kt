package com.jefisu.auth.di

import com.jefisu.auth.data.AuthRepositoryImpl
import com.jefisu.auth.domain.AuthRepository
import com.jefisu.auth.domain.validation.ValidateEmail
import com.jefisu.auth.domain.validation.ValidatePassword
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AuthModule {

    @Provides
    @ViewModelScoped
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()

    @Provides
    @ViewModelScoped
    fun provideValidateEmail() = ValidateEmail()

    @Provides
    @ViewModelScoped
    fun provideValidatePassword() = ValidatePassword()
}
