package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jefisu.authentication.presentation.authScreen
import com.jefisu.authentication.presentation.navigateAuthSignIn
import com.jefisu.authentication.presentation.navigateAuthSignUp
import com.jefisu.ui.navigation.Route
import com.jefisu.welcome.welcomeScreen

@Composable
fun NavGraph(
    startDestination: Route
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        welcomeScreen(
            onNavigateToRegisterScreen = {
                navController.navigateAuthSignUp()
            },
            onNavigateToLoginScreen = {
                navController.navigateAuthSignIn()
            }
        )
        authScreen(
            onNavigateToHomeScreen = { }
        )
    }
}