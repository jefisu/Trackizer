package com.jefisu.welcome

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeScreen

fun NavController.navigateToWelcome() = navigate(WelcomeScreen) {
    popUpTo(this@navigateToWelcome.graph.findStartDestination().id) {
        inclusive = true
    }
}

fun NavGraphBuilder.welcomeScreen() {
    composable<WelcomeScreen> {
        WelcomeScreen()
    }
}
