package com.jefisu.spending_budgets.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.common.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
object SpendingBudgetsScreen : Screen

fun NavGraphBuilder.spendingBudgetsScreen(onNavigateToSettings: () -> Unit) {
    composable<SpendingBudgetsScreen> {
        SpendingBudgetScreen(
            onNavigateToSettings = onNavigateToSettings,
        )
    }
}
