package com.jefisu.spending_budgets.presentation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object SpendingBudgetsScreen

fun NavGraphBuilder.spendingBudgetsScreen() {
    composable<SpendingBudgetsScreen> {
        val viewModel = hiltViewModel<SpendingBudgetsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpendingBudgetScreen(
            state = state
        )
    }
}
