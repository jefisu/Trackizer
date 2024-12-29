package com.jefisu.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.auth.domain.AuthRepository
import com.jefisu.auth.domain.validation.emailValidate
import com.jefisu.auth.domain.validation.passwordValidate
import com.jefisu.auth.presentation.register.util.getPasswordStrength
import com.jefisu.auth.presentation.util.asMessageText
import com.jefisu.domain.model.User
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.ui.MessageController
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
internal class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.EmailChanged -> {
                if (action.email.length > User.E_MAIL_MAX_LENGTH) return
                _state.update { it.copy(email = action.email) }
            }

            is RegisterAction.PasswordChanged -> {
                if (action.password.length > User.PASSWORD_MAX_LENGTH) return
                _state.update {
                    it.copy(
                        password = action.password,
                        passwordStrength = getPasswordStrength(action.password),
                    )
                }
            }

            is RegisterAction.Register -> register()
        }
    }

    private fun register() = viewModelScope.launch {
        val email = _state.value.email
        val password = _state.value.password

        val emailResult = emailValidate.validate(email)
        emailResult.error?.let {
            MessageController.sendMessage(it.asMessageText())
            return@launch
        }

        val passwordResult = passwordValidate.validate(password)
        if (passwordResult.error?.isNotEmpty() == true) {
            MessageController.sendMessage(passwordResult.error!!.first().asMessageText())
            return@launch
        }

        _state.update { it.copy(isLoading = true) }
        authRepository.signUp(email, password)
            .onSuccess {
                navigator.navigate(Destination.AuthenticatedGraph) {
                    popUpTo(Destination.AuthGraph) {
                        inclusive = true
                    }
                }
            }
            .onError { MessageController.sendMessage(it.asMessageText()) }
        _state.update { it.copy(isLoading = false) }
    }
}