package com.jefisu.ui.event

import com.jefisu.ui.UiEvent

sealed interface NavigationEvent : UiEvent {
    data object NavigateUp : NavigationEvent

    data class NavigateToAuth(val isLogin: Boolean) : NavigationEvent
    data object NavigateToHome : NavigationEvent
    data object NavigateToSpendingBudgets : NavigationEvent
    data class NavigateToSubscriptionInfo(val id: String) : NavigationEvent
    data object NavigateToSettings : NavigationEvent
}
