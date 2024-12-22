package com.jefisu.spending_budgets.domain.validation

import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

val categoryNameValidate = Validation<String, DataMessage> { value ->
    if (value.isBlank()) {
        return@Validation ValidationResult(
            successfully = false,
            error = DataMessage.CATEGORY_NAME_REQUIRED,
        )
    }

    ValidationResult(successfully = true)
}