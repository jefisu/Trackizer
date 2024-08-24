package com.jefisu.home.presentation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen

fun NavController.navigateHome() = navigate(HomeScreen)

fun NavGraphBuilder.homeScreen(
    navigateToSpendingBudgets: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable<HomeScreen> {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
            onNavigateToSpendingBudgets = navigateToSpendingBudgets,
            onNavigateToSettings = navigateToSettings,
        )
    }
}
