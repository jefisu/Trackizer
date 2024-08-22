package com.jefisu.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.auth.domain.AuthMessage
import com.jefisu.auth.domain.AuthRepository
import com.jefisu.auth.domain.validation.ValidateEmail
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged -> {
                state = state.copy(email = action.email)
            }

            is LoginAction.PasswordChanged -> {
                state = state.copy(password = action.password)
            }

            is LoginAction.RememberMeCredentials -> {
                state = state.copy(rememberMeCredentials = !state.rememberMeCredentials)
            }

            is LoginAction.Login -> login()

            is LoginAction.ToggleForgotPasswordBottomSheet -> {
                state = state.copy(showForgotPasswordSheet = !state.showForgotPasswordSheet)
            }

            is LoginAction.EmailResetPasswordChanged -> {
                state = state.copy(emailResetPassword = action.email)
            }

            is LoginAction.SendResetPassword -> sendResetPassword()
        }
    }

    private fun login() = viewModelScope.launch {
        with(state) {
            validateAction(email, password) {
                authRepository.signIn(email, password)
                    .onSuccess {
                        state = state.copy(isLoggedIn = true)
                    }
                    .onError { error ->
                        state = state.copy(message = error)
                    }
            }
        }
    }

    private fun sendResetPassword() {
    }

    private fun validateAction(
        email: String,
        password: String? = null,
        block: suspend () -> Unit,
    ) {
        val emailResult = validateEmail.execute(email)
        emailResult.error?.let { error ->
            state = state.copy(message = error)
            return
        }

        password?.let { pass ->
            if (pass.isBlank()) {
                state = state.copy(message = AuthMessage.Error.INVALID_EMAIL_OR_PASSWORD)
                return
            }
        }

        state = state.copy(isLoading = true)
        viewModelScope.launch {
            block()
            state = state.copy(isLoading = false)
        }
    }
}
