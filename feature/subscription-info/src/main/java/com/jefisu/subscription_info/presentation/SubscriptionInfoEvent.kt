package com.jefisu.subscription_info.presentation

import com.jefisu.ui.UiEvent

sealed interface SubscriptionInfoEvent : UiEvent {
    data object ToogleBottomSheet : SubscriptionInfoEvent
}