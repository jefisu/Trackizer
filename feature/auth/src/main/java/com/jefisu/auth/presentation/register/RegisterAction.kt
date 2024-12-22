package com.jefisu.auth.presentation.register

internal sealed interface RegisterAction {
    data class EmailChanged(val email: String) : RegisterAction
    data class PasswordChanged(val password: String) : RegisterAction
    data object Register : RegisterAction
}