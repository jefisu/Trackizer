package com.jefisu.auth.domain.validation

import android.util.Patterns
import com.jefisu.domain.util.Message

class ValidateEmail {

    fun execute(email: String): ValidationResult<Error> {
        if (email.isBlank()) {
            return ValidationResult(
                successfully = false,
                error = Error.CAN_T_BE_BLANK,
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successfully = false,
                error = Error.INVALID_FORMAT,
            )
        }
        return ValidationResult(
            successfully = true,
        )
    }

    enum class Error : Message {
        INVALID_FORMAT,
        CAN_T_BE_BLANK,
    }
}
