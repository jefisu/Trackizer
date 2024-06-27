package com.jefisu.authentication.presentation

sealed interface AuthAction {
    data class RegisterEmailChanged(val email: String) : AuthAction
    data class RegisterPasswordChanged(val password: String) : AuthAction
    data class LoginEmailChanged(val email: String) : AuthAction
    data class LoginPasswordChanged(val password: String) : AuthAction
    data object SubmitRegister : AuthAction
    data object SignUpWithGoogle : AuthAction
    data object SignUpWithFacebook : AuthAction
    data object SubmitLogin : AuthAction
    data object LoginForgotPassword : AuthAction
    data object RemeberMeCredentials : AuthAction
}