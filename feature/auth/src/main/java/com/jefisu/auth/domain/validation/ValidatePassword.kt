package com.jefisu.auth.domain.validation

import com.jefisu.domain.util.Message

class ValidatePassword {

    fun execute(password: String): ValidationResult<List<Error>> {
        val errors = mutableListOf<Error>()

        if (password.length < MIN_LENGTH) {
            errors.add(Error.TOO_SHORT)
        }

        val hasLetter = password.any { it.isLetter() }
        if (!hasLetter) {
            errors.add(Error.NO_LETTER)
        }

        val hasDigit = password.any { it.isDigit() }
        if (!hasDigit) {
            errors.add(Error.NO_DIGIT)
        }

        val hasSpecialCharacter = Regex("[^a-zA-Z0-9]").containsMatchIn(password)
        if (!hasSpecialCharacter) {
            errors.add(Error.NO_SPECIAL_CHARACTER)
        }

        return ValidationResult(
            successfully = errors.isEmpty(),
            error = errors,
        )
    }

    enum class Error : Message {
        TOO_SHORT,
        NO_LETTER,
        NO_DIGIT,
        NO_SPECIAL_CHARACTER,
    }

    companion object {
        const val MIN_LENGTH = 8
    }
}
