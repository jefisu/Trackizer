package com.jefisu.credit_cards.domain.validation

import com.jefisu.credit_cards.domain.CardMessage
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

val cardHolderValidate = Validation<String, CardMessage> { value ->
    if (value.isBlank()) {
        return@Validation ValidationResult(
            successfully = false,
            error = CardMessage.Error.EMPTY_CARD_HOLDER,
        )
    }

    ValidationResult(successfully = true)
}