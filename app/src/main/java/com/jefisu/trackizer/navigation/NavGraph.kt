package com.jefisu.trackizer.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jefisu.auth.presentation.authScreen
import com.jefisu.auth.presentation.navigateAuthSignIn
import com.jefisu.auth.presentation.navigateAuthSignUp
import com.jefisu.common.navigation.Screen
import com.jefisu.domain.repository.UserRepository
import com.jefisu.home.presentation.HomeScreen
import com.jefisu.home.presentation.homeScreen
import com.jefisu.home.presentation.navigateToHome
import com.jefisu.spending_budgets.presentation.SpendingBudgetsScreen
import com.jefisu.spending_budgets.presentation.spendingBudgetsScreen
import com.jefisu.ui.components.BottomNavItem
import com.jefisu.ui.screen.BottomNavigationScreen
import com.jefisu.welcome.WelcomeScreen
import com.jefisu.welcome.welcomeScreen
import org.koin.compose.koinInject

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()

    val context = LocalContext.current
    var selectedNavIndex by rememberSaveable { mutableIntStateOf(0) }

    BottomNavigationScreen(
        showBottomNavigation = navBackStack.showBottomNavigation(),
        selectedNavItem = BottomNavItem.entries[selectedNavIndex],
        onNavItemClick = {
            if (it in listOf(
                    BottomNavItem.ADD,
                    BottomNavItem.CALENDAR,
                    BottomNavItem.CREDIT_CARDS,
                )
            ) {
                Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show()
                return@BottomNavigationScreen
            }
            navController.navigateWithBottomNavigation(it)
            selectedNavIndex = it.ordinal
        },
    ) {
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
            spendingBudgetsScreen(onNavigateToSettings = {})
        }
    }
}

@Composable
fun startDestination(): Screen {
    val userRepository = koinInject<UserRepository>()
    return if (userRepository.isAuthenticated()) HomeScreen else WelcomeScreen
}

fun NavBackStackEntry?.showBottomNavigation(): Boolean {
    val screensToShow = listOf(
        HomeScreen,
        SpendingBudgetsScreen,
    )
    return screensToShow.any { this?.destination?.hasRoute(it::class) == true }
}

fun NavController.navigateWithBottomNavigation(navItem: BottomNavItem) {
    val screen = when (navItem) {
        BottomNavItem.HOME -> HomeScreen
        BottomNavItem.BUDGETS -> SpendingBudgetsScreen
        else -> return
    }
    navigate(screen) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
