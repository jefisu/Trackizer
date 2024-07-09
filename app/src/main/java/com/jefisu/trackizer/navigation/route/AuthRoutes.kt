package com.jefisu.trackizer.navigation.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.jefisu.auth.presentation.authScreen
import com.jefisu.auth.presentation.navigateAuthSignIn
import com.jefisu.auth.presentation.navigateAuthSignUp
import com.jefisu.welcome.WelcomeScreen
import com.jefisu.welcome.welcomeScreen
import kotlinx.serialization.Serializable

@Serializable
object AuthRoute : Route

fun NavGraphBuilder.authNav(navController: NavController) {
    navigation<AuthRoute>(
        startDestination = WelcomeScreen
    ) {
        welcomeScreen(
            onNavigateToRegisterScreen = navController::navigateAuthSignUp,
            onNavigateToLoginScreen = navController::navigateAuthSignIn,
        )
        authScreen(
            onNavigateToHomeScreen = navController::navigateToHome
        )
    }
}