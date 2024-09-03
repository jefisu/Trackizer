package com.jefisu.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.auth.domain.AuthRepository
import com.jefisu.auth.domain.validation.emailValidate
import com.jefisu.auth.domain.validation.passwordValidate
import com.jefisu.auth.presentation.register.util.getPasswordStrength
import com.jefisu.auth.presentation.util.asMessageText
import com.jefisu.ui.MessageController
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
internal class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    var state by mutableStateOf(RegisterState())
        private set

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.EmailChanged -> {
                state = state.copy(email = action.email)
            }

            is RegisterAction.PasswordChanged -> {
                state = state.copy(
                    password = action.password,
                    passwordStrength = getPasswordStrength(action.password),
                )
            }

            is RegisterAction.Register -> register()
        }
    }

    private fun register() = viewModelScope.launch {
        with(state) {
            val emailResult = emailValidate.validate(email)
            emailResult.error?.let {
                MessageController.sendMessage(it.asMessageText())
                return@launch
            }

            val passwordResult = passwordValidate.validate(password)
            passwordResult.error?.let {
                MessageController.sendMessage(it.first().asMessageText())
                return@launch
            }

            state = copy(isLoading = true)
            authRepository.signUp(email, password)
                .onSuccess { state = copy(isLoggedIn = true) }
                .onError { MessageController.sendMessage(it.asMessageText()) }
        }
    }
}
