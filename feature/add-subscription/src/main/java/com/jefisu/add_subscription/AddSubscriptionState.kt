package com.jefisu.add_subscription

import com.jefisu.domain.model.SubscriptionService

data class AddSubscriptionState(
    val price: String = "",
    val description: String = "",
    val selectedService: SubscriptionService = SubscriptionService.SPOTIFY,
)
