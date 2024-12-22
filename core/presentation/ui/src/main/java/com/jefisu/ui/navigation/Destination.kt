package com.jefisu.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object AuthGraph : Destination

    @Serializable
    data object AuthenticatedGraph : Destination

    @Serializable
    data object WelcomeScreen : Destination

    @Serializable
    data class AuthScreen(val isLogging: Boolean) : Destination

    @Serializable
    data object HomeScreen : Destination

    @Serializable
    data object SpendingBudgetsScreen : Destination

    @Serializable
    data object CalendarScreen : Destination

    @Serializable
    data object CreditCardScreen : Destination

    @Serializable
    data class AddSubscriptionScreen(val id: String?) : Destination

    @Serializable
    data class SubscriptionInfoScreen(val id: String) : Destination

    @Serializable
    data object SettingsScreen : Destination
}

val allDestinations: List<Destination>
    get() = listOf(
        Destination.AuthGraph,
        Destination.AuthenticatedGraph,
        Destination.WelcomeScreen,
        Destination.HomeScreen,
        Destination.SpendingBudgetsScreen,
        Destination.CalendarScreen,
        Destination.CreditCardScreen,
        Destination.AddSubscriptionScreen(id = null),
        Destination.SubscriptionInfoScreen(id = ""),
        Destination.SettingsScreen,
    )