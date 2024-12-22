package com.jefisu.auth.presentation.register.util

import com.jefisu.auth.domain.validation.PassswordValidateError
import com.jefisu.auth.domain.validation.passwordValidate

internal enum class PasswordStrength {
    VULNERABLE,
    WEAK,
    STRONG,
    VERY_STRONG,
}

internal fun getPasswordStrength(password: String): PasswordStrength? {
    if (password.isEmpty()) return null

    val validationResult = passwordValidate.validate(password)
    val totalErrorCriteria = PassswordValidateError.entries.size

    val passedChecks = totalErrorCriteria - (validationResult.error?.size ?: 0)

    return when (passedChecks) {
        1 -> PasswordStrength.VULNERABLE
        2 -> PasswordStrength.WEAK
        3 -> PasswordStrength.STRONG
        else -> PasswordStrength.VERY_STRONG
    }
}