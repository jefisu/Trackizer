package com.jefisu.credit_cards.presentation.util

import com.jefisu.credit_cards.R
import com.jefisu.domain.model.CardFlag
import com.steliospapamichail.creditcardmasker.utils.CardType

internal fun CardFlag.asFlagResource(): Int = when (this) {
    CardFlag.AMERICAN_EXPRESS -> R.drawable.ic_american_express
    CardFlag.MASTERCARD -> R.drawable.ic_mastercard
    CardFlag.VISA -> R.drawable.ic_visa
    CardFlag.DISCOVER -> R.drawable.ic_discover
    CardFlag.DINNERS_CLUB -> R.drawable.ic_dinners_club
    CardFlag.MAESTRO -> R.drawable.ic_maestro
    else -> R.drawable.ic_unknown_credit_card
}

internal fun CardType.asCardFlag(): CardFlag = when (this) {
    CardType.VISA -> CardFlag.VISA
    CardType.MASTERCARD -> CardFlag.MASTERCARD
    CardType.AMERICAN_EXPRESS -> CardFlag.AMERICAN_EXPRESS
    CardType.JCB -> CardFlag.JCB
    CardType.DINNERS_CLUB -> CardFlag.DINNERS_CLUB
    CardType.MAESTRO -> CardFlag.MAESTRO
    CardType.DISCOVER -> CardFlag.DISCOVER
    CardType.UNKNOWN -> CardFlag.UNKNOWN
}