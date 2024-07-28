package com.jefisu.home.presentation.util

import com.jefisu.domain.model.ServiceIcon
import com.jefisu.domain.model.Subscription
import java.time.LocalDate

internal object SampleData {

    val monthlyBudget = 1000f
    val subscriptions = (1..10).map {
        Subscription(
            id = it.toString(),
            name = "Subscription $it",
            icon = ServiceIcon.YOUTUBE_PREMIUM,
            price = it.toFloat(),
            firstPaymentDate = LocalDate.now().plusDays(it.toLong()),
            reminder = it % 2 == 0,
        )
    }
}
