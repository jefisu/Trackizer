package com.jefisu.auth.presentation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.auth.presentation.components.EndlessHorizontalPager
import com.jefisu.auth.presentation.custom_auth_provider.CustomAuthProviderRoot
import com.jefisu.auth.presentation.login.LoginScreenRoot
import com.jefisu.auth.presentation.register.RegisterScreenRoot
import com.jefisu.auth.presentation.util.AuthPage
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.TrackizerLogoBox
import com.jefisu.ui.navigation.Destination

@Composable
fun AuthScreen(navArgs: Destination.AuthScreen) {
    val startPage = when {
        navArgs.isLogging -> AuthPage.LoginWithEmail
        else -> AuthPage.CustomAuthProviders
    }

    TrackizerLogoBox {
        EndlessHorizontalPager(
            items = AuthPage.entries,
            startPage = startPage.ordinal,
            modifier = Modifier.navigationBarsPadding(),
        ) { page, onNextPageClick ->
            when (page) {
                AuthPage.CustomAuthProviders -> {
                    CustomAuthProviderRoot(
                        navigateToRegister = onNextPageClick,
                    )
                }

                AuthPage.RegisterWithEmail -> {
                    RegisterScreenRoot(
                        navigateToLogin = onNextPageClick,
                    )
                }

                AuthPage.LoginWithEmail -> {
                    LoginScreenRoot(
                        navigateToRegister = onNextPageClick,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    TrackizerTheme {
        AuthScreen(
            navArgs = Destination.AuthScreen(isLogging = false),
        )
    }
}