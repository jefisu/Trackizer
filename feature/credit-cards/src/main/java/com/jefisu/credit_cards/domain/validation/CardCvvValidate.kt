package com.jefisu.credit_cards.domain.validation

import com.jefisu.credit_cards.domain.CardConstants
import com.jefisu.credit_cards.domain.CardMessage
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

val cardCvvValidate = Validation<String, CardMessage> { value ->
    if (value.length < CardConstants.CVV_LENGTH) {
        return@Validation ValidationResult(
            successfully = false,
            error = CardMessage.Error.INVALID_CVV,
        )
    }

    ValidationResult(successfully = true)
}
