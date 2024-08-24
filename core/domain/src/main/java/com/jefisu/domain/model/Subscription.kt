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

enum class SubscriptionService(val title: String) {
    YOUTUBE_PREMIUM("YouTube Premium"),
    SPOTIFY("Spotify"),
    MICROSOFT_365("Microsoft 365"),
    NETFLIX("Netflix"),
    HBO_GO("HBO Go"),
}
