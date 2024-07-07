package com.jefisu.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


data class Size(
    val icon: IconSize = IconSize()
)

data class IconSize(
    val small: Dp = 12.dp,
    val extraSmall: Dp = 16.dp,
    val default: Dp = 18.dp,
    val medium: Dp = 20.dp,
    val extraMedium: Dp = 24.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 48.dp
)