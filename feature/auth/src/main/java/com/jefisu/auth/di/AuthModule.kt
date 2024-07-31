package com.jefisu.auth.di

import com.jefisu.auth.data.AuthRepositoryImpl
import com.jefisu.auth.domain.repository.AuthRepository
import com.jefisu.auth.domain.validation.ValidateEmail
import com.jefisu.auth.domain.validation.ValidatePassword
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginViewModel
import com.jefisu.auth.presentation.auth_provider_pages.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    scope(named(AUTH_SCOPE)) {
        scopedOf(::ValidateEmail)
        scopedOf(::ValidatePassword)
        scopedOf(::AuthRepositoryImpl).bind<AuthRepository>()
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
    }
}

const val AUTH_SCOPE = "auth_scope"
