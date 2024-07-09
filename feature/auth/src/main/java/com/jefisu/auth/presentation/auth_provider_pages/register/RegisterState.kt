package com.jefisu.auth.presentation.auth_provider_pages.register

import com.jefisu.auth.presentation.AuthState
import com.jefisu.auth.presentation.auth_provider_pages.register.util.PasswordStrength
import com.jefisu.common.util.MessageText

data class RegisterState(
    override val email: String = "",
    override val password: String = "",
    override val isLoading: Boolean = false,
    override val messageText: MessageText? = null,
    override val isLoggedIn: Boolean = false,
    val passwordStrength: PasswordStrength? = null
) : AuthState