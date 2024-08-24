package com.jefisu.designsystem.util

import com.jefisu.designsystem.R
import com.jefisu.domain.model.SubscriptionService

fun SubscriptionService.asIconResource(): Int = when (this) {
    SubscriptionService.YOUTUBE_PREMIUM -> R.drawable.ic_youtube_premium
    SubscriptionService.SPOTIFY -> R.drawable.ic_spotify
    SubscriptionService.MICROSOFT_365 -> R.drawable.ic_microsoft_365
    SubscriptionService.NETFLIX -> R.drawable.ic_netflix
    SubscriptionService.HBO_GO -> R.drawable.ic_hbo_go
}
