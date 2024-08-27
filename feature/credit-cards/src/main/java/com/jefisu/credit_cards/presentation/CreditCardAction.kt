package com.jefisu.credit_cards.presentation

import com.jefisu.domain.model.Card

sealed interface CreditCardAction {
    data class SelectCreditCard(val card: Card) : CreditCardAction
    data class ToogleAddCreditCardBottomSheet(val card: Card? = null) : CreditCardAction
    data class CardNameChanged(val cardName: String) : CreditCardAction
    data class CardHolderChanged(val cardHolder: String) : CreditCardAction
    data class CardNumberChanged(val cardNumber: String) : CreditCardAction
    data class ExpirationDateChanged(val expirationDate: String) : CreditCardAction
    data class CvvCodeChanged(val cvv: String) : CreditCardAction
    data object SaveCard : CreditCardAction
    data object CloseMessage : CreditCardAction
}
