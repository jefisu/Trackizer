package com.jefisu.auth.presentation.auth_provider_pages.login

import com.jefisu.auth.presentation.AuthState
import com.jefisu.common.util.MessageText

data class LoginState(
    override val email: String = "",
    override val password: String = "",
    override val isLoading: Boolean = false,
    override val messageText: MessageText? = null,
    override val isLoggedIn: Boolean = false,
    val rememberMeCredentials: Boolean = false,
    val showForgotPasswordSheet: Boolean = false,
    val emailResetPassword: String = ""
) : AuthState