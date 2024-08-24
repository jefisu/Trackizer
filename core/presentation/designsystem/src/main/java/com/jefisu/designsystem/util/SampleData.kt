package com.jefisu.designsystem.util

import com.jefisu.domain.model.Subscription
import com.jefisu.domain.model.SubscriptionService
import java.time.LocalDate

object SampleData {
    const val MONTHLY_BUDGET = 1000f
    val subscriptions = (0..10).map {
        val index = it % 2
        Subscription(
            id = it.toString(),
            description = "Subscription $it",
            service = SubscriptionService.entries[index],
            price = it.toFloat(),
            paymentDate = LocalDate.of(2024, 7, 10).plusDays(it.toLong()),
            reminder = index == 0,
            categoryId = "category$it",
        )
    }
}
