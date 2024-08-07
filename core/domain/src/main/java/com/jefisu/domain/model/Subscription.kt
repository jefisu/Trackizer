package com.jefisu.domain.model

import java.time.LocalDate

data class Subscription(
    val id: String,
    val categoryId: String,
    val service: SubscriptionService,
    val description: String,
    val price: Float,
    val paymentDate: LocalDate,
    val reminder: Boolean,
)
