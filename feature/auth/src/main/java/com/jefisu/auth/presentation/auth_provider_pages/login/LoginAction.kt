package com.jefisu.auth.presentation.auth_provider_pages.login

import com.jefisu.auth.data.AuthMessage

sealed interface LoginAction {
    data class EmailChanged(val email: String) : LoginAction
    data class PasswordChanged(val password: String) : LoginAction
    data object Login : LoginAction
    data object RememberMeCredentials : LoginAction
    data object ToggleForgotPasswordBottomSheet : LoginAction
    data class EmailResetPasswordChanged(val email: String) : LoginAction
    data object SendResetPassword : LoginAction
    data object CloseMessage : LoginAction
    data class ShowError(val error: AuthMessage.Error) : LoginAction
}