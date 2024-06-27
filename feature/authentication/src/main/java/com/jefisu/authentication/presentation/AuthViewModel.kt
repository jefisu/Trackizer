package com.jefisu.authentication.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    var state by mutableStateOf(AuthState())
        private set

    fun onAction(action: AuthAction) {
        when (action) {
            is AuthAction.RegisterEmailChanged -> {
                state = state.copy(registerEmail = action.email)
            }

            is AuthAction.RegisterPasswordChanged -> {
                state = state.copy(
                    registerPassword = action.password
                )
            }

            is AuthAction.LoginEmailChanged -> {
                state = state.copy(loginEmail = action.email)
            }

            is AuthAction.LoginPasswordChanged -> {
                state = state.copy(loginPassword = action.password)
            }

            is AuthAction.RemeberMeCredentials -> {
                state = state.copy(rememberMeCredentials = !state.rememberMeCredentials)
            }

            is AuthAction.LoginForgotPassword -> Unit
            is AuthAction.SubmitLogin -> Unit
            is AuthAction.SubmitRegister -> Unit
            is AuthAction.SignUpWithFacebook -> Unit
            is AuthAction.SignUpWithGoogle -> Unit
        }
    }
}