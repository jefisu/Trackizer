package com.jefisu.credit_cards.presentation

import com.jefisu.ui.UiEvent

sealed interface CreditCardUiEvent : UiEvent {
    data object HideBottomSheet : CreditCardUiEvent
}