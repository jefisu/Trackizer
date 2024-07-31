package com.jefisu.auth.presentation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jefisu.auth.di.AUTH_SCOPE
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginViewModel
import com.jefisu.auth.presentation.auth_provider_pages.register.RegisterViewModel
import com.jefisu.common.navigation.Screen
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.getKoin
import org.koin.core.qualifier.named

@Serializable
data class AuthScreen(val isLoginToStart: Boolean) : Screen

fun NavController.navigateAuthSignUp() = navigate(
    AuthScreen(isLoginToStart = false),
)

fun NavController.navigateAuthSignIn() = navigate(
    AuthScreen(isLoginToStart = true),
)

fun NavGraphBuilder.authScreen(onNavigateToHomeScreen: () -> Unit) = composable<AuthScreen> {
    val authScope = getKoin().getOrCreateScope(AUTH_SCOPE, named(AUTH_SCOPE))

    val args = it.toRoute<AuthScreen>()

    val loginViewModel: LoginViewModel = koinViewModel(scope = authScope)
    val loginState by loginViewModel.state.collectAsStateWithLifecycle()

    val registerViewModel: RegisterViewModel = koinViewModel(scope = authScope)
    val registerState by registerViewModel.state.collectAsStateWithLifecycle()

    AuthScreen(
        navArgs = args,
        loginState = loginState,
        registerState = registerState,
        onLoginAction = loginViewModel::onAction,
        onRegisterAction = registerViewModel::onAction,
        onNavigateToHome = {
            authScope.close()
            onNavigateToHomeScreen()
        },
    )
}
