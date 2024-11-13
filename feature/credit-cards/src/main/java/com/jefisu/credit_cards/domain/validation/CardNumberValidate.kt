package com.jefisu.credit_cards.domain.validation

import com.jefisu.credit_cards.domain.CardConstants
import com.jefisu.credit_cards.domain.CardMessage
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

val cardNumberValidate = Validation<String, CardMessage> { value ->
    if (value.length < CardConstants.CARD_NUMBER_LENGTH) {
        return@Validation ValidationResult(
            successfully = false,
            error = CardMessage.Error.INVALID_CARD_NUMBER,
        )
    }

    ValidationResult(successfully = true)
}
