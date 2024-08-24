package com.jefisu.designsystem

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val iconExtraSmall: Dp = 16.dp,
    val iconDefault: Dp = 24.dp,
    val iconMedium: Dp = 40.dp,
    val iconLarge: Dp = 106.dp,
    val iconExtraLarge: Dp = 161.dp,
    val appLogoSmall: Dp = 29.dp,
    val buttonHeightSmall: Dp = 36.dp,
    val buttonHeight: Dp = 48.dp,
    val buttonMaxWidth: Dp = 280.dp,
    val circularProgressSmall: Dp = 30.dp,
    val textFieldHeight: Dp = 48.dp,
)

val TrackizerTheme.size: Size get() = Size()
