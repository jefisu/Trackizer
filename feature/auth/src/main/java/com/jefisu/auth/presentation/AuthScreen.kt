package com.jefisu.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.auth.presentation.components.EndlessHorizontalPager
import com.jefisu.auth.presentation.custom_auth_provider.CustomAuthProviderRoot
import com.jefisu.auth.presentation.util.AuthPage
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.TrackizerLogoBox

@Composable
fun AuthScreen(
    navArgs: AuthRoute,
    navigateToHome: () -> Unit,
) {
    val startPage = when {
        navArgs.isLogin -> AuthPage.LoginWithEmail
        else -> AuthPage.CustomAuthProviders
    }

    TrackizerLogoBox {
        EndlessHorizontalPager(
            items = AuthPage.entries,
            startPage = startPage.ordinal,
        ) { page, onNextPageClick ->
            when (page) {
                AuthPage.CustomAuthProviders -> {
                    CustomAuthProviderRoot(
                        navigateToRegister = onNextPageClick,
                        navigateToHome = navigateToHome,
                    )
                }

                AuthPage.RegisterWithEmail -> Unit
                AuthPage.LoginWithEmail -> Unit
            }
        }
    }
}

@Preview
@Composable
private fun AuthScreenPreview() {
    TrackizerTheme {
        AuthScreen(
            navArgs = AuthRoute(),
            navigateToHome = {},
        )
    }
}
