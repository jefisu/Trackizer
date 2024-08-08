package com.jefisu.spending_budgets.presentation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.common.navigation.Screen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object SpendingBudgetsScreen : Screen

fun NavGraphBuilder.spendingBudgetsScreen(onNavigateToSettings: () -> Unit) {
    composable<SpendingBudgetsScreen> {
        val viewModel = koinViewModel<SpendingBudgetsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpendingBudgetScreen(
            state = state,
            onNavigateToSettings = onNavigateToSettings,
        )
    }
}
