package com.jefisu.spending_budgets.presentation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.spendingBudgetsScreen() {
    composable<Destination.SpendingBudgetsScreen> {
        val viewModel = hiltViewModel<SpendingBudgetsViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        SpendingBudgetScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}
