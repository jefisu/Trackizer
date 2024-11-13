package com.jefisu.designsystem

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

object TrackizerTheme

@Composable
fun TrackizerTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalTextStyle provides TrackizerTheme.typography.bodyLarge,
    ) {
        Surface(
            color = Gray80,
            contentColor = Color.White,
            content = content,
        )
    }
}
