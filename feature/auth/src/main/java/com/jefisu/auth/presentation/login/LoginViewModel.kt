package com.jefisu.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.auth.domain.AuthMessage
import com.jefisu.auth.domain.AuthRepository
import com.jefisu.auth.domain.validation.emailValidate
import com.jefisu.auth.presentation.util.asMessageText
import com.jefisu.domain.repository.UserRepository
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.ui.MessageController
import com.jefisu.ui.UiEventController
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart { rememberUserEmail() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            LoginState(),
        )

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged -> {
                _state.update { it.copy(email = action.email) }
            }

            is LoginAction.PasswordChanged -> {
                _state.update { it.copy(password = action.password) }
            }

            is LoginAction.RememberMeCredentials -> {
                _state.update { it.copy(rememberMeCredentials = !it.rememberMeCredentials) }
            }

            is LoginAction.Login -> login()

            is LoginAction.EmailResetPasswordChanged -> {
                _state.update { it.copy(emailResetPassword = action.email) }
            }

            is LoginAction.SendResetPassword -> sendResetPassword()
        }
    }

    private fun rememberUserEmail() {
        viewModelScope.launch {
            val user = userRepository.user.firstOrNull()
            _state.update {
                it.copy(
                    email = user?.email.orEmpty(),
                    rememberMeCredentials = user != null,
                )
            }
        }
    }

    private fun login() = viewModelScope.launch {
        val email = _state.value.email
        val password = _state.value.password
        validateAction(email, password) {
            authRepository.signIn(email, password)
                .onSuccess {
                    navigator.navigate(Destination.AuthenticatedGraph) {
                        popUpTo(Destination.AuthGraph) {
                            inclusive = true
                        }
                    }
                }
                .onError { error ->
                    MessageController.sendMessage(error.asMessageText())
                }
        }
    }

    private fun sendResetPassword() {
        val emailResetPassword = _state.value.emailResetPassword
        validateAction(emailResetPassword) {
            authRepository.sendPasswordResetEmail(emailResetPassword)
                .onSuccess { message ->
                    MessageController.sendMessage(message.asMessageText())
                    _state.update { it.copy(emailResetPassword = "") }
                    UiEventController.sendEvent(LoginEvent.HideForgotPasswordBottomSheet)
                }
                .onError { error ->
                    MessageController.sendMessage(error.asMessageText())
                }
        }
    }

    private fun validateAction(
        email: String,
        password: String? = null,
        block: suspend () -> Unit,
    ) {
        viewModelScope.launch {
            val emailResult = emailValidate.validate(email)
            emailResult.error?.let { error ->
                MessageController.sendMessage(error.asMessageText())
                return@launch
            }

            password?.let { pass ->
                if (pass.isBlank()) {
                    MessageController.sendMessage(
                        AuthMessage.Error.INVALID_EMAIL_OR_PASSWORD.asMessageText(),
                    )
                    return@launch
                }
            }

            _state.update { it.copy(isLoading = true) }
            block()
            _state.update { it.copy(isLoading = false) }
        }
    }
}