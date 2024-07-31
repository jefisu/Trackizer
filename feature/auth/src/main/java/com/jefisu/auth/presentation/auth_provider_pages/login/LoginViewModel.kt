package com.jefisu.auth.presentation.auth_provider_pages.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.auth.data.AuthMessage
import com.jefisu.auth.domain.repository.AuthRepository
import com.jefisu.auth.domain.validation.ValidateEmail
import com.jefisu.auth.presentation.util.asMessageText
import com.jefisu.common.util.onError
import com.jefisu.common.util.onSuccess
import com.jefisu.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    init {
        rememberUserEmail()
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged -> _state.update {
                it.copy(email = action.email)
            }

            is LoginAction.PasswordChanged -> _state.update {
                it.copy(password = action.password)
            }

            is LoginAction.RememberMeCredentials -> _state.update {
                it.copy(rememberMeCredentials = !it.rememberMeCredentials)
            }

            is LoginAction.Login -> login()

            is LoginAction.ToggleForgotPasswordBottomSheet -> _state.update {
                it.copy(showForgotPasswordSheet = !it.showForgotPasswordSheet)
            }

            is LoginAction.EmailResetPasswordChanged -> _state.update {
                it.copy(emailResetPassword = action.email)
            }

            is LoginAction.SendResetPassword -> sendResetPassword()

            is LoginAction.ShowError -> _state.update {
                it.copy(messageText = action.error.asMessageText())
            }

            is LoginAction.CloseMessage -> _state.update {
                it.copy(messageText = null)
            }
        }
    }

    private fun rememberUserEmail() = viewModelScope.launch {
        val emailSaved = userRepository.emailFlow.firstOrNull()
        _state.update {
            it.copy(
                email = emailSaved.orEmpty(),
                rememberMeCredentials = emailSaved != null,
            )
        }
    }

    private fun login() = viewModelScope.launch {
        val email = _state.value.email
        val password = _state.value.password
        val rememberMeCredentials = _state.value.rememberMeCredentials
        validateBeforeRepositoryAction(email, password) {
            authRepository.signIn(email, password)
                .onSuccess {
                    _state.update { it.copy(isLoggedIn = true) }
                    if (rememberMeCredentials) {
                        userRepository.saveEmail(email)
                    } else {
                        userRepository.deleteSavedEmail()
                    }
                }
                .onError { error ->
                    _state.update { it.copy(messageText = error.asMessageText()) }
                }
        }
    }

    private fun sendResetPassword() {
        val email = _state.value.emailResetPassword
        validateBeforeRepositoryAction(email) {
            authRepository.sendPasswordResetEmail(email)
                .onSuccess { message ->
                    _state.update {
                        it.copy(
                            messageText = message.asMessageText(),
                            showForgotPasswordSheet = false,
                            emailResetPassword = "",
                        )
                    }
                }
                .onError { error ->
                    _state.update { it.copy(messageText = error.asMessageText()) }
                }
        }
    }

    private fun validateBeforeRepositoryAction(
        email: String,
        password: String? = null,
        block: suspend () -> Unit,
    ) {
        val emailResult = validateEmail.execute(email)
        emailResult.error?.let { error ->
            _state.update { it.copy(messageText = error.asMessageText()) }
            return
        }

        password?.let { pass ->
            if (pass.isBlank()) {
                _state.update {
                    it.copy(
                        messageText = AuthMessage.Error
                            .INVALID_EMAIL_OR_PASSWORD
                            .asMessageText(),
                    )
                }
                return
            }
        }

        _state.update {
            it.copy(
                isLoading = true,
                messageText = null,
            )
        }
        viewModelScope.launch {
            block()
            _state.update { it.copy(isLoading = false) }
        }
    }
}
