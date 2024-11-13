package com.jefisu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastFirstOrNull
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jefisu.ui.ObserveAsEvents

val LocalNavigator = compositionLocalOf<Navigator> { error("No navigator provided") }

@Composable
fun NavigationRoot(
    navigator: Navigator,
    destinations: List<Destination>,
    animation: NavigationAnimation = remember { NavigationAnimation() },
    builder: NavGraphBuilder.() -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ObserveAsEvents(navigator.actions) { action ->
        when (action) {
            NavigatorAction.NavigateUp -> navController.navigateUp()
            is NavigatorAction.Navigate -> navController.navigate(
                action.destination,
                action.navOptions,
            )
        }
    }

    LaunchedEffect(navBackStackEntry) {
        val destination = destinations.fastFirstOrNull {
            navBackStackEntry?.isCurrentDestination(it) == true
        }
        destination?.also(navigator::updateCurrentDestination)
    }

    CompositionLocalProvider(
        LocalNavigator provides navigator,
    ) {
        NavHost(
            navController = navController,
            startDestination = navigator.startDestination,
            builder = builder,
            enterTransition = {
                animation.enterTransition(this)
            },
            exitTransition = {
                animation.exitTransition(this)
            },
            popEnterTransition = {
                animation.enterTransition(this, isPopAnimation = true)
            },
            popExitTransition = {
                animation.exitTransition(this, isPopAnimation = true)
            },
        )
    }
}

fun NavBackStackEntry.isCurrentDestination(destination: Destination): Boolean {
    return this.destination.hasRoute(destination::class)
}