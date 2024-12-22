package com.jefisu.credit_cards.presentation.util

import com.jefisu.credit_cards.R
import com.jefisu.credit_cards.domain.CardMessage
import com.jefisu.domain.util.Argument
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.UiText

fun CardMessage.asMessageText(): MessageText = when (this) {
    is CardMessage.Error -> MessageText.Error(
        when (this) {
            CardMessage.Error.EMPTY_CARD_NAME -> UiText.StringResource(
                R.string.empty_data,
                arrayOf(Argument(R.string.card_name, Argument.Format.CAPITALIZE)),
            )

            CardMessage.Error.EMPTY_CARD_HOLDER -> UiText.StringResource(
                R.string.empty_data,
                arrayOf(Argument(R.string.card_holder, Argument.Format.CAPITALIZE)),
            )

            CardMessage.Error.INVALID_EXPIRATION_DATE -> UiText.StringResource(
                R.string.invalid_data,
                arrayOf(Argument(R.string.expiration_date, Argument.Format.CAPITALIZE)),
            )

            CardMessage.Error.INVALID_CVV -> UiText.StringResource(
                R.string.invalid_data,
                arrayOf(Argument(R.string.cvv, Argument.Format.UPPERCASE)),
            )

            CardMessage.Error.INVALID_CARD_NUMBER -> UiText.StringResource(
                R.string.invalid_data,
                arrayOf(Argument(R.string.card_number, Argument.Format.CAPITALIZE)),
            )
        },
    )
}