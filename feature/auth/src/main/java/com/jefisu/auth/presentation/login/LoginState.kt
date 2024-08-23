package com.jefisu.auth.presentation.login

import com.jefisu.domain.util.MessageText

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val message: MessageText? = null,
    val isLoggedIn: Boolean = false,
    val rememberMeCredentials: Boolean = false,
    val showForgotPasswordSheet: Boolean = false,
    val emailResetPassword: String = "",
)
