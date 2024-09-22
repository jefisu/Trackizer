package com.jefisu.domain.model.util

import com.jefisu.domain.model.Subscription
import java.time.LocalDate

fun List<Subscription>.filterUpcomingBills(): List<Subscription> {
    val today = LocalDate.now()
    return filter { sub ->
        sub.reminder ||
            sub.paymentDate.monthValue >= today.monthValue
    }.map { sub ->
        if (sub.reminder) {
            val paymentDate = sub.paymentDate
            val difference = today.monthValue - paymentDate.monthValue
            val newPaymentDate = paymentDate.plusMonths(difference.toLong())
            sub.copy(
                paymentDate = newPaymentDate,
            )
        } else {
            sub
        }
    }.sortedByDescending { it.paymentDate }
}