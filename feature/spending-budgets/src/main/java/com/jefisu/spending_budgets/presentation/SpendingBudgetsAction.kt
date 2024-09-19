package com.jefisu.spending_budgets.presentation

import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType

sealed interface SpendingBudgetsAction {
    data class CategoryNameChanged(val name: String) : SpendingBudgetsAction
    data class CategoryBudgetChanged(val budget: String) : SpendingBudgetsAction
    data class CategorTypeChanged(val type: CategoryType) : SpendingBudgetsAction
    data class ToggleAddCategoryBottomSheet(val category: Category? = null) : SpendingBudgetsAction
    data object ToogleCategoryTypePicker : SpendingBudgetsAction
    data object AddCategory : SpendingBudgetsAction
}