package com.jefisu.home.presentation.util

import com.jefisu.domain.model.Subscription
import com.jefisu.domain.model.SubscriptionService
import java.time.LocalDate

internal object SampleData {

    val monthlyBudget = 1000f
    val subscriptions = (1..10).map {
        Subscription(
            id = it.toString(),
            description = "Subscription $it",
            service = SubscriptionService.YOUTUBE_PREMIUM,
            price = it.toFloat(),
            paymentDate = LocalDate.now().plusDays(it.toLong()),
            reminder = it % 2 == 0,
            categoryId = "category$it",
        )
    }
}
