@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.ui.sharedtransition

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalInspectionMode

fun Modifier.sharedTransition(
    block: @Composable SharedTransitionScope.(AnimatedVisibilityScope) -> Modifier,
) = composed {
    val isPreviewMode = LocalInspectionMode.current
    if (isPreviewMode) return@composed this

    val sharedTransitionScope = LocalSharedTransitionScope.current
    if (sharedTransitionScope == null) return@composed this

    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current
    if (animatedVisibilityScope == null) return@composed this

    with(sharedTransitionScope) {
        block(animatedVisibilityScope)
    }
}