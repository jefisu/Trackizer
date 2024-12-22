package com.jefisu.auth.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.authScreen() {
    composable<Destination.AuthScreen> {
        val navArgs = it.toRoute<Destination.AuthScreen>()
        AuthScreen(navArgs = navArgs)
    }
}