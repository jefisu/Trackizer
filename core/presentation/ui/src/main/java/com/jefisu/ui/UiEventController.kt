package com.jefisu.ui

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface UiEvent

object UiEventController {

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    suspend fun sendEvent(uiEvent: UiEvent) {
        _events.emit(uiEvent)
    }
}
