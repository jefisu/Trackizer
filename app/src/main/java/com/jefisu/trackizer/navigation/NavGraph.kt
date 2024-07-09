package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.jefisu.data.repository.UserRepository
import com.jefisu.trackizer.navigation.route.AuthRoute
import com.jefisu.trackizer.navigation.route.HomeRoute
import com.jefisu.trackizer.navigation.route.Route
import com.jefisu.trackizer.navigation.route.authNav
import com.jefisu.trackizer.navigation.route.homeNav
import org.koin.compose.koinInject

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = getStartDestination(),
    ) {
        authNav(navController)
        homeNav()
    }
}

@Composable
fun getStartDestination(): Route {
    val userRepository = koinInject<UserRepository>()
    return if (userRepository.isAuthenticated()) {
        HomeRoute
    } else AuthRoute
}