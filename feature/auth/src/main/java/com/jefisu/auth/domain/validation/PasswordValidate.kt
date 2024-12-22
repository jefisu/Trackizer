package com.jefisu.auth.domain.validation

import com.jefisu.domain.util.Message
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

internal val passwordValidate = Validation<String, List<PassswordValidateError>> { value ->
    val errors = mutableListOf<PassswordValidateError>()

    if (value.length < MIN_LENGTH) {
        errors.add(PassswordValidateError.TOO_SHORT)
    }

    val hasLetter = value.any { it.isLetter() }
    if (!hasLetter) {
        errors.add(PassswordValidateError.NO_LETTER)
    }

    val hasDigit = value.any { it.isDigit() }
    if (!hasDigit) {
        errors.add(PassswordValidateError.NO_DIGIT)
    }

    val hasSpecialCharacter = Regex("[^a-zA-Z0-9]").containsMatchIn(value)
    if (!hasSpecialCharacter) {
        errors.add(PassswordValidateError.NO_SPECIAL_CHARACTER)
    }

    ValidationResult(
        successfully = errors.isEmpty(),
        error = errors,
    )
}

enum class PassswordValidateError : Message {
    TOO_SHORT,
    NO_LETTER,
    NO_DIGIT,
    NO_SPECIAL_CHARACTER,
}

const val MIN_LENGTH = 8