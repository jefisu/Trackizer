package com.jefisu.auth.presentation.login

import com.jefisu.auth.domain.AuthMessage

sealed interface LoginEvent {
    data object CloseMessage : LoginEvent
    data class ShowError(val error: AuthMessage) : LoginEvent
}
