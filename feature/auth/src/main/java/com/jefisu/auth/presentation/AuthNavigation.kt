package com.jefisu.auth.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class AuthRoute(val isLogin: Boolean = false)

fun NavController.navigateToAuth(isLogin: Boolean) = navigate(
    AuthRoute(isLogin = isLogin),
)

fun NavGraphBuilder.authScreen() {
    composable<AuthRoute> {
        val navArgs = it.toRoute<AuthRoute>()
        AuthScreen(navArgs = navArgs)
    }
}
