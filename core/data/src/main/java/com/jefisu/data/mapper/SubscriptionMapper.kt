package com.jefisu.data.mapper

import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.remote.document.SubscriptionDocument
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.model.SubscriptionService
import java.time.LocalDate
import org.mongodb.kbson.ObjectId

fun Subscription.toSubscriptionOffline(): SubscriptionOffline {
    val subscription = this
    return SubscriptionOffline().apply {
        if (subscription.id.isNotEmpty()) {
            _id = ObjectId(subscription.id)
        }
        serviceName = subscription.service.name
        description = subscription.description
        price = subscription.price
        firstPaymentEpochDay = subscription.firstPayment.toEpochDay()
        reminder = subscription.reminder
    }
}

fun SubscriptionOffline.toSubscription() = Subscription(
    id = _id.toHexString(),
    service = SubscriptionService.valueOf(serviceName),
    description = description,
    price = price,
    firstPayment = LocalDate.ofEpochDay(firstPaymentEpochDay),
    reminder = reminder,
)

fun SubscriptionOffline.toSubscriptionDocument() = SubscriptionDocument(
    id = cloudId,
    offlineId = _id.toHexString(),
    serviceName = serviceName,
    description = description,
    price = price,
    firstPaymentEpochDay = firstPaymentEpochDay,
    reminder = reminder,
)

fun SubscriptionDocument.toSubscriptionOffline(): SubscriptionOffline {
    val subscription = this
    return SubscriptionOffline().apply {
        _id = ObjectId(subscription.offlineId)
        cloudId = subscription.id
        serviceName = subscription.serviceName
        description = subscription.description
        price = subscription.price
        firstPaymentEpochDay = subscription.firstPaymentEpochDay
        reminder = subscription.reminder
    }
}
