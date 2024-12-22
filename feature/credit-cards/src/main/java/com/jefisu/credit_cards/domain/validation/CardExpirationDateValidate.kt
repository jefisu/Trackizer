package com.jefisu.credit_cards.domain.validation

import androidx.core.text.isDigitsOnly
import com.jefisu.credit_cards.domain.CardConstants
import com.jefisu.credit_cards.domain.CardMessage
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult
import java.time.LocalDate

val cardExpirationDateValidate = Validation<String, CardMessage> { value ->
    if (value.length < CardConstants.EXPIRATION_DATE_LENGTH) {
        return@Validation ValidationResult(
            successfully = false,
            error = CardMessage.Error.INVALID_EXPIRATION_DATE,
        )
    }

    val (month, year) = value.take(2) to value.takeLast(2)

    val isMonthValid = month.isDigitsOnly() && month.toIntOrNull() in 1..12
    if (!isMonthValid) {
        return@Validation ValidationResult(
            successfully = false,
            error = CardMessage.Error.INVALID_EXPIRATION_DATE,
        )
    }

    val yearNow = LocalDate.now().year % 100
    val isYearValid = year.isDigitsOnly() && year.toIntOrNull()?.let { it >= yearNow } ?: false
    if (!isYearValid) {
        return@Validation ValidationResult(
            successfully = false,
            error = CardMessage.Error.INVALID_EXPIRATION_DATE,
        )
    }

    ValidationResult(successfully = true)
}