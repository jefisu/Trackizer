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

fun NavController.navigateToHome() = navigate(HomeScreen) {
    launchSingleTop = true
}

fun NavGraphBuilder.homeScreen() {
    composable<HomeScreen> {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
        )
    }
}
