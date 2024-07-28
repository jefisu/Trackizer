package com.jefisu.home.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.common.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
object HomeScreen : Screen

fun NavController.navigateToHome() = navigate(HomeScreen)

fun NavGraphBuilder.homeScreen(onNavigateToSettings: () -> Unit) {
    composable<HomeScreen> {
        HomeScreen(
            state = HomeState(),
            onNavigateToSettings = onNavigateToSettings,
        )
    }
}
