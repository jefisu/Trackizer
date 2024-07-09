package com.jefisu.auth.presentation

import com.jefisu.common.util.MessageText

interface AuthState {
    val email: String
    val password: String
    val isLoading: Boolean
    val messageText: MessageText?
    val isLoggedIn: Boolean
}