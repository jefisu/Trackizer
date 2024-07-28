package com.jefisu.trackizer.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jefisu.auth.presentation.authScreen
import com.jefisu.auth.presentation.navigateAuthSignIn
import com.jefisu.auth.presentation.navigateAuthSignUp
import com.jefisu.common.navigation.Screen
import com.jefisu.data.repository.UserRepository
import com.jefisu.home.presentation.HomeScreen
import com.jefisu.home.presentation.homeScreen
import com.jefisu.home.presentation.navigateToHome
import com.jefisu.ui.components.StandardBottomNavigation
import com.jefisu.welcome.WelcomeScreen
import com.jefisu.welcome.welcomeScreen
import org.koin.compose.koinInject

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination(),
        ) {
            welcomeScreen(
                onNavigateToRegisterScreen = navController::navigateAuthSignUp,
                onNavigateToLoginScreen = navController::navigateAuthSignIn,
            )
            authScreen(
                onNavigateToHomeScreen = navController::navigateToHome,
            )
            homeScreen(onNavigateToSettings = {})
        }

        AnimatedVisibility(
            visible = navBackStack.showBottomNavigation(),
            modifier = Modifier.align(Alignment.BottomStart),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            StandardBottomNavigation(
                onNavClick = {},
            )
        }
    }
}

@Composable
fun startDestination(): Screen {
    val userRepository = koinInject<UserRepository>()
    return if (userRepository.isAuthenticated()) HomeScreen else WelcomeScreen
}

fun NavBackStackEntry?.showBottomNavigation() = this?.destination?.hasRoute<HomeScreen>() == true
