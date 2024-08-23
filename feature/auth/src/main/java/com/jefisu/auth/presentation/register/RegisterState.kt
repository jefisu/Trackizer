package com.jefisu.auth.presentation.register

import com.jefisu.auth.presentation.register.util.PasswordStrength
import com.jefisu.domain.util.MessageText

internal data class RegisterState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val messageText: MessageText? = null,
    val isLoggedIn: Boolean = false,
    val passwordStrength: PasswordStrength? = null,
)
