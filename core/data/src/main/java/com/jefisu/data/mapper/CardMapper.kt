package com.jefisu.data.mapper

import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.remote.document.CardDocument
import com.jefisu.domain.model.Card
import com.jefisu.domain.model.CardFlag
import com.jefisu.domain.model.CardType
import java.time.LocalDate
import org.mongodb.kbson.BsonObjectId

fun Card.toCardOffline(): CardOffline {
    val card = this
    return CardOffline().apply {
        if (card.id.isNotEmpty()) {
            _id = BsonObjectId(card.id)
        }
        name = card.name
        cardHolder = card.cardHolder
        number = card.number
        expirationEpochDay = card.expirationDate.toEpochDay()
        cvv = card.cvv
        flagName = card.flag.name
        typeName = card.type.name
    }
}

fun CardOffline.toCard() = Card(
    id = _id.toHexString(),
    name = name,
    cardHolder = cardHolder,
    number = number,
    expirationDate = LocalDate.ofEpochDay(expirationEpochDay),
    cvv = cvv,
    flag = CardFlag.valueOf(flagName),
    type = CardType.valueOf(typeName),
)

fun CardOffline.toCardDocument() = CardDocument(
    id = _id.toHexString(),
    name = name,
    cardHolder = cardHolder,
    number = number,
    expirationEpochDay = expirationEpochDay,
    cvv = cvv,
    flagName = flagName,
    typeName = typeName,
)

fun CardDocument.toCardOffline(): CardOffline {
    val card = this
    return CardOffline().apply {
        _id = BsonObjectId(card.id)
        name = card.name
        cardHolder = card.cardHolder
        number = card.number
        expirationEpochDay = card.expirationEpochDay
        cvv = card.cvv
        flagName = card.flagName
        typeName = card.typeName
    }
}
