package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jefisu.auth.presentation.authScreen
import com.jefisu.auth.presentation.navigateAuthSignIn
import com.jefisu.auth.presentation.navigateAuthSignUp
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
            navigateToRegisterScreen = navController::navigateAuthSignUp,
            navigateToLoginScreen = navController::navigateAuthSignIn,
        )
        authScreen(
            navigateToHome = {},
        )
    }
}
