package com.jefisu.designsystem.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

interface UiEvent

object UiEventController {

    private val _events = Channel<UiEvent>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(uiEvent: UiEvent) {
        _events.send(uiEvent)
    }
}
