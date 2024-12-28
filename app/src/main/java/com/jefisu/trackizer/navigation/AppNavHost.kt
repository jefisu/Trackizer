package com.jefisu.trackizer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.util.fastAny
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.jefisu.add_subscription.presentation.AddSubscriptionScreen
import com.jefisu.auth.presentation.AuthScreen
import com.jefisu.calendar.presentation.CalendarScreen
import com.jefisu.credit_cards.presentation.CreditCardsScreen
import com.jefisu.designsystem.components.TrackizerBottomNavigation
import com.jefisu.home.presentation.HomeScreen
import com.jefisu.settings.presentation.SettingsScreen
import com.jefisu.spending_budgets.presentation.SpendingBudgetsScreen
import com.jefisu.subscription_info.presentation.SubscriptionInfoScreen
import com.jefisu.ui.navigation.AnimationConfig
import com.jefisu.ui.navigation.AnimationType
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.NavigationAnimation
import com.jefisu.ui.navigation.NavigationRoot
import com.jefisu.ui.navigation.Navigator
import com.jefisu.ui.navigation.allDestinations
import com.jefisu.user.presentation.editprofile.EditProfileScreen
import com.jefisu.welcome.WelcomeScreen
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

    val navigationAnimation = remember {
        NavigationAnimation(
            animConfigs = listOf(
                AnimationConfig(
                    destinations = listOf(
                        Destination.SettingsScreen,
                        Destination.EditProfileScreen,
                    ),
                    type = AnimationType.HORIZONTAL,
                ),
                AnimationConfig(
                    destinations = listOf(
                        Destination.WelcomeScreen,
                        Destination.AuthScreen(isLogging = false),
                        Destination.SubscriptionInfoScreen(id = ""),
                        Destination.AddSubscriptionScreen(id = null),
                    ),
                    type = AnimationType.VERTICAL,
                ),
            ),
        )
    }

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
            animation = navigationAnimation,
        ) {
            navigation<Destination.AuthGraph>(
                startDestination = Destination.WelcomeScreen,
            ) {
                composable<Destination.WelcomeScreen> {
                    WelcomeScreen()
                }
                composable<Destination.AuthScreen> {
                    AuthScreen(
                        navArgs = it.toRoute(),
                    )
                }
            }
            navigation<Destination.AuthenticatedGraph>(
                startDestination = Destination.HomeScreen,
            ) {
                composable<Destination.HomeScreen> {
                    HomeScreen()
                }
                composable<Destination.SpendingBudgetsScreen> {
                    SpendingBudgetsScreen()
                }
                composable<Destination.CalendarScreen> {
                    CalendarScreen()
                }
                composable<Destination.CreditCardScreen> {
                    CreditCardsScreen()
                }
                composable<Destination.AddSubscriptionScreen> {
                    AddSubscriptionScreen()
                }
                composable<Destination.SubscriptionInfoScreen> {
                    SubscriptionInfoScreen()
                }
                composable<Destination.SettingsScreen> {
                    SettingsScreen()
                }
                composable<Destination.EditProfileScreen> {
                    EditProfileScreen()
                }
            }
        }
    }
}