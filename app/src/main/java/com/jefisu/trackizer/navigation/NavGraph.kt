package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jefisu.ui.navigation.Screen
import com.jefisu.welcome.welcomeScreen

@Composable
fun NavGraph(startDestination: Screen) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        welcomeScreen(
            onNavigateToRegisterScreen = {},
            onNavigateToLoginScreen = {},
        )
    }
}
