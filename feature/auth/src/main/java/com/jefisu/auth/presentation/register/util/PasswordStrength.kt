package com.jefisu.auth.presentation.register.util

import com.jefisu.auth.domain.validation.ValidatePassword

internal enum class PasswordStrength {
    VULNERABLE,
    WEAK,
    STRONG,
    VERY_STRONG,
}

internal fun getPasswordStrength(password: String): PasswordStrength? {
    if (password.isEmpty()) return null

    val validationResult = ValidatePassword().execute(password)
    val totalErrorCriteria = ValidatePassword.Error.entries.size

    val passedChecks = totalErrorCriteria - (validationResult.error?.size ?: 0)

    return when (passedChecks) {
        1 -> PasswordStrength.VULNERABLE
        2 -> PasswordStrength.WEAK
        3 -> PasswordStrength.STRONG
        else -> PasswordStrength.VERY_STRONG
    }
}
