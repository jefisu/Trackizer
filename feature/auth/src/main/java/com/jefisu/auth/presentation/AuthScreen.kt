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
            modifier = Modifier.navigationBarsPadding(),
        ) { page, onNextPageClick ->
            when (page) {
                AuthPage.CustomAuthProviders -> {
                    CustomAuthProviderRoot(
                        navigateToRegister = onNextPageClick,
                        navigateToHome = navigateToHome,
                    )
                }

                AuthPage.RegisterWithEmail -> {
                    RegisterScreenRoot(
                        navigateToHome = navigateToHome,
                        navigateToLogin = onNextPageClick,
                    )
                }

                AuthPage.LoginWithEmail -> {
                    LoginScreenRoot(
                        navigateToHome = navigateToHome,
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
            navArgs = AuthRoute(),
            navigateToHome = {},
        )
    }
}
