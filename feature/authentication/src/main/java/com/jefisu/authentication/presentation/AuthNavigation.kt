package com.jefisu.authentication.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jefisu.ui.navigation.Route
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class AuthScreenRoute(
    val startOption: String
) : Route {
    companion object {
        const val SIGN_UP = "sign_up"
        const val SIGN_IN = "sign_in"
    }
}

fun NavController.navigateAuthSignUp() = navigate(
    AuthScreenRoute(AuthScreenRoute.SIGN_UP)
)

fun NavController.navigateAuthSignIn() = navigate(
    AuthScreenRoute(AuthScreenRoute.SIGN_IN)
)

fun NavGraphBuilder.authScreen(
    onNavigateToHomeScreen: () -> Unit
) = composable<AuthScreenRoute> {
    val args = it.toRoute<AuthScreenRoute>()
    val viewModel = koinViewModel<AuthViewModel>()
    val state = viewModel.state

    LaunchedEffect(key1 = state) {
        if (state.hasUserLogged) {
            onNavigateToHomeScreen()
        }
    }

    AuthScreen(
        navArgs = args,
        state = state,
        onAction = viewModel::onAction
    )
}