package com.jefisu.designsystem

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val iconDefault: Dp = 24.dp,
    val iconMedium: Dp = 40.dp,
    val iconLarge: Dp = 106.dp,
    val iconExtraLarge: Dp = 161.dp,
    val appLogoSmall: Dp = 29.dp,
    val buttonHeight: Dp = 48.dp,
    val buttonMaxWidth: Dp = 280.dp,
)

val TrackizerTheme.size: Size get() = Size()
