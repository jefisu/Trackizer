package com.jefisu.credit_cards.presentation

import com.jefisu.domain.model.Card
import com.jefisu.domain.model.CardFlag
import com.jefisu.domain.model.SubscriptionService

data class CreditCardState(
    val creditCards: Map<Card, List<SubscriptionService>> = emptyMap(),
    val selectedCard: Card? = null,
    val showAddCreditCardBottomSheet: Boolean = false,
    val editCard: Card? = null,
    val cardName: String = "",
    val cardHolder: String = "",
    val cardNumber: String = "",
    val expirationDate: String = "",
    val cvv: String = "",
    val flag: CardFlag = CardFlag.UNKNOWN,
    val showDeleteAlert: Boolean = false,
)
