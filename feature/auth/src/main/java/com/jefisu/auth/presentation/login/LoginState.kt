package com.jefisu.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val rememberMeCredentials: Boolean = false,
    val emailResetPassword: String = "",
)
