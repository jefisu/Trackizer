package com.jefisu.auth.presentation.auth_provider_pages.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.auth.domain.repository.AuthRepository
import com.jefisu.auth.domain.validation.ValidateEmail
import com.jefisu.auth.domain.validation.ValidatePassword
import com.jefisu.auth.presentation.auth_provider_pages.register.util.getPasswordStrength
import com.jefisu.auth.presentation.util.asMessageText
import com.jefisu.common.util.onError
import com.jefisu.common.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.EmailChanged -> _state.update {
                it.copy(email = action.email)
            }

            is RegisterAction.PasswordChanged -> _state.update {
                it.copy(
                    password = action.password,
                    passwordStrength = getPasswordStrength(action.password)
                )
            }

            is RegisterAction.Register -> register()

            is RegisterAction.CloseMessage -> _state.update {
                it.copy(messageText = null)
            }
        }
    }

    private fun register() = viewModelScope.launch {
        val (email, password) = _state.value

        validateEmail.execute(email).also { result ->
            result.error?.let { error ->
                _state.update {
                    it.copy(
                        messageText = error.asMessageText()
                    )
                }
                return@launch
            }
        }
        validatePassword.execute(password).also { result ->
            result.error?.let { errors ->
                if (errors.isNotEmpty()) {
                    _state.update {
                        it.copy(
                            messageText = errors
                                .first()
                                .asMessageText()
                        )
                    }
                    return@launch
                }
            }
        }

        _state.update {
            it.copy(
                isLoading = true,
                messageText = null,
                isLoggedIn = false
            )
        }
        authRepository.signUp(email, password)
            .onSuccess {
                _state.update { it.copy(isLoggedIn = true) }
            }
            .onError { error ->
                _state.update {
                    it.copy(
                        messageText = error.asMessageText()
                    )
                }
            }
        _state.update { it.copy(isLoading = false) }
    }
}