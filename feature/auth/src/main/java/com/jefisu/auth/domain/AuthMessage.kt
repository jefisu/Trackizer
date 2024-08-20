package com.jefisu.auth.domain

import com.jefisu.domain.util.Message

sealed interface AuthMessage : Message {

    enum class Error : AuthMessage {
        INVALID_EMAIL_OR_PASSWORD,
        USER_ALREADY_EXISTS,
        GOOGLE_FAILED_TO_LOGIN,
        FACEBOOK_FAILED_TO_LOGIN,
        USER_NOT_FOUND,
        SERVER_ERROR,
        INTERNET_UNAVAILABLE,
    }

    enum class Success : AuthMessage {
        SENT_PASSWORD_RESET_EMAIL,
    }
}
