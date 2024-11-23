package com.jefisu.spending_budgets.presentation

import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType

data class SpendingBudgetsState(
    val categories: List<Category> = emptyList(),
    val category: Category? = null,
    val categoryName: String = "",
    val categoryBudget: String = "",
    val categoryType: CategoryType = CategoryType.Entertainment,
)
