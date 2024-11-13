package com.jefisu.welcome

import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.LocalNavigator
import kotlinx.coroutines.launch

fun NavGraphBuilder.welcomeScreen() {
    composable<Destination.WelcomeScreen> {
        val scope = rememberCoroutineScope()
        val navigator = LocalNavigator.current

        WelcomeScreen(
            onNavigate = {
                scope.launch { navigator.navigate(it) }
            },
        )
    }
}
