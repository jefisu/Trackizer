package com.jefisu.ui.sharedtransition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.compositionLocalOf

val LocalAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }