package com.jefisu.add_subscription.domain.validation

import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

val subscriptionPriceValidate = Validation<Float, DataMessage> { value ->
    if (value == 0f) {
        return@Validation ValidationResult(
            successfully = false,
            error = DataMessage.SUBSCRIPTION_PRICE_REQUIRED,
        )
    }

    ValidationResult(successfully = true)
}