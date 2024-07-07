package com.jefisu.welcome

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Screen
import kotlinx.serialization.Serializable

@Serializable
object WelcomeScreen : Screen

fun NavGraphBuilder.welcomeScreen(
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToLoginScreen: () -> Unit,
) {
    composable<WelcomeScreen> {
        WelcomeScreen(onNavigateToRegisterScreen, onNavigateToLoginScreen)
    }
}
