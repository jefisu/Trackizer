package com.jefisu.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val navigator: Navigator,
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val _user = userRepository.user.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5.seconds),
        null,
    ).value

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

            is LoginAction.EmailResetPasswordChanged -> {
                state = state.copy(emailResetPassword = action.email)
            }

            is LoginAction.SendResetPassword -> sendResetPassword()
        }
    }

    private fun rememberUserEmail() {
        viewModelScope.launch {
            state = state.copy(
                email = _user?.email.orEmpty(),
                rememberMeCredentials = _user != null,
            )
        }
    }

    private fun login() = viewModelScope.launch {
        with(state) {
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
    }

    private fun sendResetPassword() {
        with(state) {
            validateAction(emailResetPassword) {
                authRepository.sendPasswordResetEmail(emailResetPassword)
                    .onSuccess { message ->
                        MessageController.sendMessage(message.asMessageText())
                        state = copy(emailResetPassword = "")
                        UiEventController.sendEvent(LoginEvent.HideForgotPasswordBottomSheet)
                    }
                    .onError { error ->
                        MessageController.sendMessage(error.asMessageText())
                    }
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

            state = state.copy(isLoading = true)
            block()
            state = state.copy(isLoading = false)
        }
    }
}