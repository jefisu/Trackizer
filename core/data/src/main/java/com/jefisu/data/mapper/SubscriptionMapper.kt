package com.jefisu.data.mapper

import com.jefisu.data.dto.SubscriptionDto
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.model.SubscriptionService
import java.time.LocalDate
import java.time.ZoneId

fun SubscriptionDto.toSubscription() = Subscription(
    id = id,
    service = SubscriptionService.valueOf(serviceName),
    description = description,
    price = price,
    paymentDate = LocalDate.ofInstant(firstPayment.toInstant(), ZoneId.systemDefault()),
    reminder = reminder,
    categoryId = categoryId,
)
