package com.jefisu.home.presentation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.common.navigation.Screen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object HomeScreen : Screen

fun NavController.navigateToHome() = navigate(HomeScreen)

fun NavGraphBuilder.homeScreen(onNavigateToSettings: () -> Unit) {
    composable<HomeScreen> {
        val viewModel: HomeViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
            onNavigateToSettings = onNavigateToSettings,
        )
    }
}
