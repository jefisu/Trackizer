package com.jefisu.designsystem.util

import androidx.compose.ui.graphics.Color
import com.jefisu.designsystem.AccentPrimary50
import com.jefisu.designsystem.AccentSecondary100
import com.jefisu.designsystem.Primary10
import com.jefisu.domain.model.CategoryType
import com.jefisu.ui.R

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

fun CategoryType.asNameResource(): Int = when (this) {
    CategoryType.Transport -> R.string.transport
    CategoryType.Entertainment -> R.string.entertainment
    CategoryType.Security -> R.string.security
}