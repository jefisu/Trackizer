package com.jefisu.ui.util

import androidx.compose.ui.graphics.Color
import com.jefisu.domain.model.CategoryType
import com.jefisu.ui.R
import com.jefisu.ui.theme.AccentPrimary50
import com.jefisu.ui.theme.AccentSecondary100
import com.jefisu.ui.theme.Primary10

fun CategoryType.asIconResource(): Int = when (this) {
    CategoryType.Transport -> R.drawable.ic_transport
    CategoryType.Entertainment -> R.drawable.ic_entertainment
    CategoryType.Security -> R.drawable.ic_security
}

fun CategoryType.asColor(): Color = when (this) {
    CategoryType.Transport -> AccentSecondary100
    CategoryType.Entertainment -> AccentPrimary50
    CategoryType.Security -> Primary10
}
