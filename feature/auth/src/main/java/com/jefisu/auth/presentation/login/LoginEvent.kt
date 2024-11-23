package com.jefisu.auth.presentation.login

import com.jefisu.ui.UiEvent

sealed interface LoginEvent: UiEvent {
    data object HideForgotPasswordBottomSheet : LoginEvent
}
