package com.jefisu.trackizer.navigation.route

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute : Route

fun NavController.navigateToHome() {
    navigate(HomeRoute) {
        popUpTo(this@navigateToHome.graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.homeNav() {
    navigation<HomeRoute>(
        startDestination = HomeScreen
    ) {
        composable<HomeScreen> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = "Home", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Serializable
object HomeScreen