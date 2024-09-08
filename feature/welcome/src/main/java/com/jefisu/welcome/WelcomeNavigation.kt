package com.jefisu.welcome

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object WelcomeScreen

fun NavGraphBuilder.welcomeScreen() {
    composable<WelcomeScreen> {
        WelcomeScreen()
    }
}
