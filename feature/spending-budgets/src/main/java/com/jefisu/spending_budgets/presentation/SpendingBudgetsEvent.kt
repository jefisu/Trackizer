package com.jefisu.spending_budgets.presentation

import com.jefisu.ui.UiEvent

internal sealed interface SpendingBudgetsEvent : UiEvent {

    data object HideAddCategoryBottomSheet : SpendingBudgetsEvent
}
