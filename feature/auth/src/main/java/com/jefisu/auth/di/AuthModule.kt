package com.jefisu.auth.di

import com.jefisu.auth.data.AuthRepositoryImpl
import com.jefisu.auth.domain.repository.AuthRepository
import com.jefisu.auth.domain.validation.ValidateEmail
import com.jefisu.auth.domain.validation.ValidatePassword
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginViewModel
import com.jefisu.auth.presentation.auth_provider_pages.register.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(Dispatchers::IO)
    singleOf(::ValidateEmail)
    singleOf(::ValidatePassword)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}