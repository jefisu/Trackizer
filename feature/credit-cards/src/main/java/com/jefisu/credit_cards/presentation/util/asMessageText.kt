package com.jefisu.credit_cards.presentation.util

import com.jefisu.credit_cards.R
import com.jefisu.credit_cards.domain.CardMessage
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.UiText

fun CardMessage.asMessageText(): MessageText = when (this) {
    is CardMessage.Error -> MessageText.Error(
        when (this) {
            CardMessage.Error.EMPTY_CARD_NAME -> UiText.StringResource(
                R.string.empty_card_name,
            )

            CardMessage.Error.EMPTY_CARD_HOLDER -> UiText.StringResource(
                R.string.empty_card_holder,
            )

            CardMessage.Error.INVALID_EXPIRATION_DATE -> UiText.StringResource(
                R.string.invalid_expiration_date,
            )

            CardMessage.Error.INVALID_CVV -> UiText.StringResource(
                R.string.invalid_cvv,
            )

            CardMessage.Error.INVALID_CARD_NUMBER -> UiText.StringResource(
                R.string.invalid_card_number,
            )
        },
    )
}
