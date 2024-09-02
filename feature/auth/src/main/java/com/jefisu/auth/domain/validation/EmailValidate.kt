package com.jefisu.auth.domain.validation

import android.util.Patterns
import com.jefisu.domain.util.Message
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

val emailValidate = Validation<String, EmailValidateError> { value ->
    if (value.isBlank()) {
        return@Validation ValidationResult(
            successfully = false,
            error = EmailValidateError.CAN_T_BE_BLANK,
        )
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
        return@Validation ValidationResult(
            successfully = false,
            error = EmailValidateError.INVALID_FORMAT,
        )
    }

    ValidationResult(successfully = true)
}

enum class EmailValidateError : Message {
    INVALID_FORMAT,
    CAN_T_BE_BLANK,
}
