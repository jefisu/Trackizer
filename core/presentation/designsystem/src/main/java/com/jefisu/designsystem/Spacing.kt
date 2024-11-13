package com.jefisu.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val small: Dp = 8.dp,
    val extraSmall: Dp = 12.dp,
    val medium: Dp = 16.dp,
    val extraMedium: Dp = 24.dp,
    val large: Dp = 32.dp,
    val extraLarge: Dp = 64.dp,
)

val LocalSpacing = compositionLocalOf { Spacing() }

val TrackizerTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
