package com.jefisu.domain.model

import java.time.LocalDate

data class Subscription(
    val id: String,
    val name: String,
    val icon: ServiceIcon,
    val price: Float,
    val firstPaymentDate: LocalDate,
    val reminder: Boolean,
)
