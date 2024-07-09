package com.jefisu.auth.presentation.auth_provider_pages.register

sealed interface RegisterAction {
    data class EmailChanged(val email: String) : RegisterAction
    data class PasswordChanged(val password: String) : RegisterAction
    data object Register : RegisterAction
    data object CloseMessage: RegisterAction
}