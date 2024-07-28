package com.jefisu.ui.util

import com.jefisu.domain.model.ServiceIcon
import com.jefisu.ui.R

fun ServiceIcon.asIconResource(): Int {
    return when(this) {
        ServiceIcon.YOUTUBE_PREMIUM -> R.drawable.ic_youtube_premium
        ServiceIcon.SPOTIFY -> R.drawable.ic_spotify
        ServiceIcon.MICROSOFT_365 -> R.drawable.ic_microsoft_365
        ServiceIcon.NETFLIX -> R.drawable.ic_netflix
        ServiceIcon.HBO_GO -> R.drawable.ic_hbo_go
    }
}