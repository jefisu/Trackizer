package com.jefisu.domain.model

import java.time.LocalDate

data class Card(
    val id: String,
    val name: String,
    val cardHolder: String,
    val number: String,
    val expirationDate: LocalDate,
    val cvv: String,
    val flag: CardFlag,
    val type: CardType,
)

enum class CardFlag {
    VISA,
    MASTERCARD,
    AMERICAN_EXPRESS,
    JCB,
    DINNERS_CLUB,
    MAESTRO,
    DISCOVER,
    UNKNOWN,
}

enum class CardType {
    CREDIT,
    DEBIT,
}
