package com.jefisu.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.auth.domain.AuthMessage
import com.jefisu.auth.domain.AuthRepository
import com.jefisu.auth.domain.validation.ValidateEmail
import com.jefisu.auth.presentation.util.asMessageText
import com.jefisu.domain.repository.UserRepository
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val validateEmail: ValidateEmail,
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    init {
        rememberUserEmail()
    }

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

            is LoginAction.CloseMessage -> {
                state = state.copy(message = null)
            }
        }
    }

    private fun rememberUserEmail() {
        viewModelScope.launch {
            val emailSaved = userRepository.email.firstOrNull()
            state = state.copy(
                email = emailSaved.orEmpty(),
                rememberMeCredentials = emailSaved != null,
            )
        }
    }

    private fun login() = viewModelScope.launch {
        with(state) {
            validateAction(email, password) {
                authRepository.signIn(email, password)
                    .onSuccess {
                        state = copy(isLoggedIn = true)
                        if (rememberMeCredentials) {
                            userRepository.rememberEmail(email)
                        } else {
                            userRepository.forgetEmail()
                        }
                    }
                    .onError { error ->
                        state = copy(message = error.asMessageText())
                    }
            }
        }
    }

    private fun sendResetPassword() {
        with(state) {
            validateAction(emailResetPassword) {
                authRepository.sendPasswordResetEmail(emailResetPassword)
                    .onSuccess { message ->
                        state = copy(
                            message = message.asMessageText(),
                            showForgotPasswordSheet = false,
                            emailResetPassword = "",
                        )
                    }
                    .onError { error ->
                        state = copy(message = error.asMessageText())
                    }
            }
        }
    }

    private fun validateAction(
        email: String,
        password: String? = null,
        block: suspend () -> Unit,
    ) {
        val emailResult = validateEmail.execute(email)
        emailResult.error?.let { error ->
            state = state.copy(message = error.asMessageText())
            return
        }

        password?.let { pass ->
            if (pass.isBlank()) {
                state = state.copy(
                    message = AuthMessage.Error
                        .INVALID_EMAIL_OR_PASSWORD
                        .asMessageText(),
                )
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
