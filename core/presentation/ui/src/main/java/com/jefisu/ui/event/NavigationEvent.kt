package com.jefisu.ui.event

import com.jefisu.ui.UiEvent

sealed interface NavigationEvent : UiEvent {
    data object NavigateBack : NavigationEvent
    data object NavigateUp : NavigationEvent
}