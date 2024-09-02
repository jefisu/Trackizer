package com.jefisu.designsystem.util

import com.jefisu.domain.util.MessageText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MessageController {

    private val _message = MutableStateFlow<MessageText?>(null)
    val message = _message.asStateFlow()

    fun sendMessage(message: MessageText) {
        _message.value = message
    }

    fun closeMessage() {
        _message.value = null
    }
}
