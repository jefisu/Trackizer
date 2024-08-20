package com.jefisu.auth.presentation.custom_auth_provider

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jefisu.auth.domain.AuthMessage

class CustomAuthProviderViewModel : ViewModel() {

    var error by mutableStateOf<AuthMessage?>(null)
        private set

    fun showError(message: AuthMessage) {
        error = message
    }

    fun closeError() {
        error = null
    }
}
