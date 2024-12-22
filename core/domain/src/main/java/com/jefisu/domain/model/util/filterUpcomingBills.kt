package com.jefisu.domain.model.util

import com.jefisu.domain.model.Subscription
import java.time.LocalDate
import java.time.MonthDay

fun List<Subscription>.filterUpcomingBills(
    currentDate: LocalDate = LocalDate.now(),
    isPerDay: Boolean = false,
): List<Subscription> {
    return filter { sub -> currentDate >= sub.firstPayment || sub.reminder }.map { sub ->
        if (sub.reminder) {
            val paymentDate = sub.firstPayment
            val difference = currentDate.monthValue - paymentDate.monthValue
            val newPaymentDate = paymentDate.plusMonths(difference.toLong())
            if (newPaymentDate.isBefore(paymentDate)) {
                return@map sub
            }
            sub.copy(firstPayment = newPaymentDate)
        } else {
            sub
        }
    }
        .filter {
            if (isPerDay) {
                val subMonthDay = MonthDay.from(it.firstPayment)
                val currentDateMonthDay = MonthDay.from(currentDate)
                return@filter subMonthDay == currentDateMonthDay
            }
            true
        }
        .sortedByDescending { it.firstPayment }
}