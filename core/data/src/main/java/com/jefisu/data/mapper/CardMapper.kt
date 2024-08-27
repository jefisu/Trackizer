package com.jefisu.data.mapper

import com.google.firebase.Timestamp
import com.jefisu.data.dto.CardDto
import com.jefisu.domain.model.Card
import com.jefisu.domain.model.CardFlag
import com.jefisu.domain.model.CardType
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun CardDto.toCard() = Card(
    id = id.orEmpty(),
    name = name,
    cardHolder = cardHolder,
    number = number,
    expirationDate = LocalDate.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault()),
    cvv = cvv,
    flag = CardFlag.valueOf(flag),
    type = CardType.valueOf(type),
)

fun Card.toCardDto() = CardDto(
    name = name,
    cardHolder = cardHolder,
    number = number,
    expirationDate = Timestamp(
        Date.from(expirationDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
    ),
    cvv = cvv,
    flag = flag.name,
    type = type.name,
    id = id.ifEmpty { null },
)
