package com.jefisu.credit_cards.presentation

import com.jefisu.designsystem.util.UiEvent

sealed interface CreditCardUiEvent : UiEvent {
    data object HideBottomSheet : CreditCardUiEvent
}
