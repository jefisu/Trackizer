package com.jefisu.settings.presentation

import com.jefisu.ui.UiEvent

sealed interface SettingsEvent : UiEvent {
    data object DeletedAccount : SettingsEvent
}