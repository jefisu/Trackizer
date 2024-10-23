package com.jefisu.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.util.fastFirstOrNull
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
            val destinationName = it::class.simpleName.orEmpty()
            navBackStackEntry?.destination?.route?.contains(destinationName) ?: false
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
        )
    }
}
