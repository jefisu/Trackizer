package com.jefisu.authentication.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.authentication.presentation.pages.LoginEmailPage
import com.jefisu.authentication.presentation.pages.RegisterAnotherAccountsPage
import com.jefisu.authentication.presentation.pages.RegisterEmailPage
import com.jefisu.ui.components.StandardScreenWithLogo
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    navArgs: AuthScreenRoute,
    state: AuthState,
    onAction: (AuthAction) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { AuthPage.entries.size },
        initialPage = if (navArgs.startOption == AuthScreenRoute.SIGN_IN) {
            AuthPage.SIGN_IN_EMAIL.ordinal
        } else {
            AuthPage.SIGN_UP_ANOTHER_ACCOUNTS.ordinal
        }
    )
    val scope = rememberCoroutineScope()
    val modifierPage = Modifier.padding(Theme.spacing.extraMedium)

    fun PagerState.navigateToAnimatedPage(page: AuthPage) {
        scope.launch {
            animateScrollToPage(
                page.ordinal,
                animationSpec = tween(
                    durationMillis = 500
                )
            )
        }
    }

    StandardScreenWithLogo {
        HorizontalPager(
            state = pagerState,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.matchParentSize(),
            userScrollEnabled = false
        ) { pageIndex ->
            when (AuthPage.entries[pageIndex]) {
                AuthPage.SIGN_UP_EMAIL -> {
                    RegisterEmailPage(
                        modifier = modifierPage,
                        state = state,
                        onAction = onAction,
                        onNavigateToLoginPage = {
                            pagerState.navigateToAnimatedPage(AuthPage.SIGN_IN_EMAIL)
                        }
                    )
                }

                AuthPage.SIGN_UP_ANOTHER_ACCOUNTS -> {
                    RegisterAnotherAccountsPage(
                        modifier = modifierPage,
                        onAction = onAction,
                        onNavigateToRegisterEmailPage = {
                            pagerState.navigateToAnimatedPage(AuthPage.SIGN_UP_EMAIL)
                        }
                    )
                }

                AuthPage.SIGN_IN_EMAIL -> {
                    LoginEmailPage(
                        modifier = modifierPage,
                        state = state,
                        onAction = onAction,
                        onNavigateToRegisterPage = {
                            pagerState.navigateToAnimatedPage(AuthPage.SIGN_UP_ANOTHER_ACCOUNTS)
                        }
                    )
                }
            }
        }
    }
}

enum class AuthPage {
    SIGN_UP_EMAIL,
    SIGN_UP_ANOTHER_ACCOUNTS,
    SIGN_IN_EMAIL
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    AppTheme {
        AuthScreen(
            state = AuthState(),
            navArgs = AuthScreenRoute(
                startOption = AuthScreenRoute.SIGN_IN
            ),
            onAction = { }
        )
    }
}