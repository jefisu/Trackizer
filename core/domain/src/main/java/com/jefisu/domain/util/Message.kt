package com.jefisu.common.util

interface Message

sealed class MessageText(val text: UiText) : Message {
    class Success(text: UiText) : MessageText(text)
    class Error(text: UiText) : MessageText(text)
    class Warning(text: UiText) : MessageText(text)
    class Help(text: UiText) : MessageText(text)
}
