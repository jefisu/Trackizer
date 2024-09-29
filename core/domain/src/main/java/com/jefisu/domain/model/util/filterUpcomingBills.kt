package com.jefisu.domain.model.util

import com.jefisu.domain.model.Subscription
import java.time.LocalDate

fun List<Subscription>.filterUpcomingBills(): List<Subscription> {
    val today = LocalDate.now()
    return filter { sub ->
        sub.reminder ||
            sub.firstPayment.monthValue >= today.monthValue
    }.map { sub ->
        if (sub.reminder) {
            val paymentDate = sub.firstPayment
            val difference = today.monthValue - paymentDate.monthValue
            val newPaymentDate = paymentDate.plusMonths(difference.toLong())
            sub.copy(
                firstPayment = newPaymentDate,
            )
        } else {
            sub
        }
    }.sortedByDescending { it.firstPayment }
}