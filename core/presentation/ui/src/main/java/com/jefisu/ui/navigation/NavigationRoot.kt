package com.jefisu.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.util.fastFirstOrNull
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jefisu.ui.ObserveAsEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        SafeNavigationArea(
            navController = navController,
            blockInteractionMillis = animation.animDuration.toLong(),
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
}

fun NavBackStackEntry.isCurrentDestination(destination: Destination): Boolean =
    this.destination.hasRoute(destination::class)

@Composable
fun SafeNavigationArea(
    navController: NavController,
    blockInteractionMillis: Long,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()

    content()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    navController.currentBackStackEntryFlow
                        .filterNotNull()
                        .distinctUntilChanged()
                        .onEach {
                            placeable.place(0, 0)
                            delay(blockInteractionMillis)
                            placeable.place(placeable.width, placeable.height)
                        }
                        .launchIn(scope)
                }
            }
            .pointerInput(Unit) {},
    )
}