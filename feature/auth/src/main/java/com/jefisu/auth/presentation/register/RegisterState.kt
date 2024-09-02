package com.jefisu.auth.presentation.register

import com.jefisu.auth.presentation.register.util.PasswordStrength

internal data class RegisterState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val passwordStrength: PasswordStrength? = null,
)
