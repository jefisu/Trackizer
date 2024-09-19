package com.jefisu.spending_budgets.domain.validation

import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.validation.Validation
import com.jefisu.domain.validation.ValidationResult

val categoryBudgetValidate = Validation<Float, DataMessage> { value ->
    if (value == 0f) {
        return@Validation ValidationResult(
            successfully = false,
            error = DataMessage.CATEGORY_BUDGET_CAN_T_BE_ZERO,
        )
    }

    ValidationResult(successfully = true)
}
