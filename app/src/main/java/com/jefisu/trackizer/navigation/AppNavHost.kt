package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jefisu.welcome.WelcomeScreen
import com.jefisu.welcome.welcomeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WelcomeScreen,
    ) {
        welcomeScreen(
            navigateToRegisterScreen = {},
            navigateToLoginScreen = {},
        )
    }
}