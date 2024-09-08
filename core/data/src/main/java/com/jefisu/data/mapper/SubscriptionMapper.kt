package com.jefisu.data.mapper

import com.google.firebase.Timestamp
import com.jefisu.data.dto.SubscriptionDto
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.model.SubscriptionService
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

fun SubscriptionDto.toSubscription() = Subscription(
    id = id.orEmpty(),
    service = SubscriptionService.valueOf(serviceName),
    description = description,
    price = price,
    paymentDate = LocalDate.ofInstant(firstPayment.toInstant(), ZoneId.systemDefault()),
    reminder = reminder,
    category = null,
    card = null,
)

fun Subscription.toSubscriptionDto() = SubscriptionDto(
    id = id.ifEmpty { null },
    categoryId = category?.id,
    cardId = card?.id,
    serviceName = service.name,
    description = description,
    price = price,
    firstPayment = Timestamp(
        Date.from(Instant.from(paymentDate.atStartOfDay(ZoneId.systemDefault()).toInstant())),
    ),
    reminder = reminder,
)
