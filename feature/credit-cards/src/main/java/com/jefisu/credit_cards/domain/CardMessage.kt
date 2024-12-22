package com.jefisu.credit_cards.domain

import com.jefisu.domain.util.Message

sealed interface CardMessage : Message {

    enum class Error : CardMessage {
        EMPTY_CARD_NAME,
        EMPTY_CARD_HOLDER,
        INVALID_EXPIRATION_DATE,
        INVALID_CVV,
        INVALID_CARD_NUMBER,
    }
}