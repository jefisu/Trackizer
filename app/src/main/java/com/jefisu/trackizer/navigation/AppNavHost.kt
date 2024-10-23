package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.fastAny
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.navigation
import com.jefisu.add_subscription.presentation.addSubscriptionScreen
import com.jefisu.auth.presentation.authScreen
import com.jefisu.calendar.presentation.calendarScreen
import com.jefisu.credit_cards.presentation.creditCardScreen
import com.jefisu.designsystem.components.TrackizerBottomNavigation
import com.jefisu.home.presentation.homeScreen
import com.jefisu.settings.presentation.settingsScreen
import com.jefisu.spending_budgets.presentation.spendingBudgetsScreen
import com.jefisu.subscription_info.presentation.subscriptionInfoScreen
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.NavigationRoot
import com.jefisu.ui.navigation.Navigator
import com.jefisu.ui.navigation.allDestinations
import com.jefisu.welcome.welcomeScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(navigator: Navigator) {
    val scope = rememberCoroutineScope()
    val currentDestination by navigator.currentDestination.collectAsStateWithLifecycle()

    val destinationsBottomNav = listOf(
        Destination.HomeScreen,
        Destination.SpendingBudgetsScreen,
        Destination.CalendarScreen,
        Destination.CreditCardScreen,
    )

    TrackizerBottomNavigation(
        visible = destinationsBottomNav.fastAny { it == currentDestination },
        selectedDestination = currentDestination,
        onNavigateClick = {
            scope.launch {
                navigator.navigate(it) {
                    popUpTo(destinationsBottomNav.first()) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        },
    ) {
        NavigationRoot(
            navigator = navigator,
            destinations = allDestinations,
        ) {
            navigation<Destination.AuthGraph>(
                startDestination = Destination.WelcomeScreen,
            ) {
                welcomeScreen()
                authScreen()
            }
            navigation<Destination.AuthenticatedGraph>(
                startDestination = Destination.HomeScreen,
            ) {
                homeScreen()
                spendingBudgetsScreen()
                calendarScreen()
                creditCardScreen()
                addSubscriptionScreen()
                subscriptionInfoScreen()
                settingsScreen()
            }
        }
    }
}
