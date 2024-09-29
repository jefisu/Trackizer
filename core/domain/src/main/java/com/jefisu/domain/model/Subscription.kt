package com.jefisu.domain.model

import java.time.LocalDate

data class Subscription(
    val service: SubscriptionService,
    val description: String,
    val price: Float,
    val firstPayment: LocalDate,
    val reminder: Boolean,
    val category: Category? = null,
    val card: Card? = null,
    val id: String = "",
)

enum class SubscriptionService(val title: String) {
    YOUTUBE_PREMIUM("YouTube Premium"),
    SPOTIFY("Spotify"),
    MICROSOFT_365("Microsoft 365"),
    NETFLIX("Netflix"),
    HBO_GO("HBO Go"),
}
