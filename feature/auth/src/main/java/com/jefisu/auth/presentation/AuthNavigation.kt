package com.jefisu.auth.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class AuthRoute(val isLogin: Boolean = false)

fun NavController.navigateAuthSignUp() = navigate(
    AuthRoute(isLogin = false),
)

fun NavController.navigateAuthSignIn() = navigate(
    AuthRoute(isLogin = true),
)

fun NavGraphBuilder.authScreen(navigateToHome: () -> Unit) {
    composable<AuthRoute> {
        val navArgs = it.toRoute<AuthRoute>()
        AuthScreen(
            navArgs = navArgs,
            navigateToHome = navigateToHome,
        )
    }
}
