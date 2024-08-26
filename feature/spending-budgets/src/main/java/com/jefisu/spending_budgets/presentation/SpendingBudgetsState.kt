package com.jefisu.spending_budgets.presentation

import com.jefisu.domain.model.Category

data class SpendingBudgetsState(val categories: List<Category> = emptyList())
