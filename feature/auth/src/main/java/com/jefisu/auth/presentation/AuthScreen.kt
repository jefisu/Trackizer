package com.jefisu.auth.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.auth.presentation.auth_provider_pages.custom_login_providers.CustomLoginProvidersPage
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginAction
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginPage
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginState
import com.jefisu.auth.presentation.auth_provider_pages.register.RegisterAction
import com.jefisu.auth.presentation.auth_provider_pages.register.RegisterPage
import com.jefisu.auth.presentation.auth_provider_pages.register.RegisterState
import com.jefisu.auth.presentation.components.EndlessHorizontalPager
import com.jefisu.auth.presentation.util.AuthPage
import com.jefisu.ui.components.FlashMessageContainer
import com.jefisu.ui.components.StandardScreenLogo
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Theme

@Composable
fun AuthScreen(
    navArgs: AuthScreen = AuthScreen(isLoginToStart = false),
    loginState: LoginState,
    registerState: RegisterState,
    onLoginAction: (LoginAction) -> Unit,
    onRegisterAction: (RegisterAction) -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val pages = AuthPage.entries

    LaunchedEffect(loginState.isLoggedIn, registerState.isLoggedIn) {
        if (loginState.isLoggedIn || registerState.isLoggedIn) {
            onNavigateToHome()
        }
    }

    FlashMessageContainer(
        message = loginState.messageText ?: registerState.messageText,
        onDismiss = {
            onLoginAction(LoginAction.CloseMessage)
            onRegisterAction(RegisterAction.CloseMessage)
        }
    ) {
        StandardScreenLogo {
            EndlessHorizontalPager(
                items = pages,
                startPage = pages.indexOf(
                    if (navArgs.isLoginToStart) AuthPage.LoginWithEmail
                    else AuthPage.CustomLoginProviders
                ),
                modifier = Modifier
                    .matchParentSize()
                    .navigationBarsPadding()
            ) { page, onClickNextPage ->
                Box(
                    modifier = Modifier.padding(Theme.spacing.extraMedium),
                ) {
                    when (page) {
                        AuthPage.RegisterWithEmail -> {
                            RegisterPage(
                                state = registerState,
                                onAction = onRegisterAction,
                                onNavigateToLoginPage = onClickNextPage,
                            )
                        }

                        AuthPage.CustomLoginProviders -> {
                            CustomLoginProvidersPage(
                                onNavigateToHome = onNavigateToHome,
                                onShowError = { onLoginAction(LoginAction.ShowError(it)) },
                                onNavigateToRegisterEmailPage = onClickNextPage,
                            )
                        }

                        AuthPage.LoginWithEmail -> {
                            LoginPage(
                                state = loginState,
                                onAction = onLoginAction,
                                onNavigateToRegisterPage = onClickNextPage
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun AuthScreenPreview() {
    AppTheme {
        AuthScreen(
            loginState = LoginState(),
            registerState = RegisterState(),
            onLoginAction = {},
            onRegisterAction = {},
            onNavigateToHome = {},
        )
    }
}
