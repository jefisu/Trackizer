package com.jefisu.welcome

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
object WelcomeScreenRoute: Route

fun NavHostController.navigateToWelcome() = navigate(WelcomeScreenRoute)

fun NavGraphBuilder.welcomeScreen(
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToLoginScreen: () -> Unit
) {
    composable<WelcomeScreenRoute> {
        WelcomeScreen(onNavigateToRegisterScreen, onNavigateToLoginScreen)
    }
}