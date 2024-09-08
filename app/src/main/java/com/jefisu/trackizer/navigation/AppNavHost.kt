package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jefisu.add_subscription.addSubscriptionScreen
import com.jefisu.add_subscription.navigateAddSubscription
import com.jefisu.auth.presentation.authScreen
import com.jefisu.auth.presentation.navigateToAuth
import com.jefisu.calendar.presentation.CalendarScreen
import com.jefisu.calendar.presentation.calendarScreen
import com.jefisu.credit_cards.presentation.CreditCardScreen
import com.jefisu.credit_cards.presentation.creditCardScreen
import com.jefisu.designsystem.components.BottomNavItem
import com.jefisu.designsystem.components.TrackizerBottomNavigation
import com.jefisu.home.presentation.HomeScreen
import com.jefisu.home.presentation.homeScreen
import com.jefisu.home.presentation.navigateToHome
import com.jefisu.spending_budgets.presentation.SpendingBudgetsScreen
import com.jefisu.spending_budgets.presentation.spendingBudgetsScreen
import com.jefisu.subscription_info.presentation.navigateToSubscriptionInfo
import com.jefisu.subscription_info.presentation.subscriptionInfoScreen
import com.jefisu.ui.ObserveAsEvents
import com.jefisu.ui.UiEventController
import com.jefisu.ui.event.NavigationEvent
import com.jefisu.welcome.welcomeScreen

private val bottomScreens = listOf(
    HomeScreen,
    SpendingBudgetsScreen,
    CalendarScreen,
    CreditCardScreen,
)

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Any,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var selectedNavIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        snapshotFlow { navBackStackEntry }.collect { entry ->
            selectedNavIndex = bottomScreens
                .indexOfFirst { entry?.destination?.hasRoute(it::class) == true }
                .coerceAtLeast(0)
        }
    }

    ObserveAsEvents(UiEventController.events) { event ->
        when (event) {
            is NavigationEvent.NavigateUp -> navController.navigateUp()
            is NavigationEvent.NavigateToAuth -> navController.navigateToAuth(event.isLogin)

            is NavigationEvent.NavigateToSubscriptionInfo -> {
                navController.navigateToSubscriptionInfo(event.id)
            }

            is NavigationEvent.NavigateToHome -> navController.navigateToHome()
            is NavigationEvent.NavigateToSpendingBudgets -> {
                navController.navigate(SpendingBudgetsScreen)
            }

            is NavigationEvent.NavigateToSettings -> Unit
            else -> Unit
        }
    }

    TrackizerBottomNavigation(
        isVisibleBottmNav = navBackStackEntry.showBottomNavigation(),
        selectedNavItem = BottomNavItem.entries[selectedNavIndex],
        onFabClick = {
            navController.navigateAddSubscription()
        },
        onNavClick = navController::navigateBottomNav,
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
        ) {
            welcomeScreen()
            authScreen()
            homeScreen()
            spendingBudgetsScreen()
            calendarScreen()
            creditCardScreen()
            addSubscriptionScreen()
            subscriptionInfoScreen()
        }
    }
}

fun NavController.navigateBottomNav(navItem: BottomNavItem) {
    val screen: Any = when (navItem) {
        BottomNavItem.HOME -> HomeScreen
        BottomNavItem.BUDGETS -> SpendingBudgetsScreen
        BottomNavItem.CALENDAR -> CalendarScreen
        BottomNavItem.CREDIT_CARDS -> CreditCardScreen
    }
    navigate(screen) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavBackStackEntry?.showBottomNavigation(): Boolean = bottomScreens.any {
    this?.destination?.hasRoute(it::class) == true
}
