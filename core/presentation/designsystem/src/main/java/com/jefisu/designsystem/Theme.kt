package com.jefisu.designsystem

import android.app.Activity
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.jefisu.ui.screen.LocalScreenIsSmall

object TrackizerTheme

@Composable
fun TrackizerTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    val config = LocalConfiguration.current
    val isSmallScreen = (config.screenWidthDp / config.screenHeightDp.toFloat()) > 0.5f

    CompositionLocalProvider(
        LocalTextStyle provides TrackizerTheme.typography.bodyLarge,
        LocalScreenIsSmall provides isSmallScreen,
    ) {
        Surface(
            color = Gray80,
            contentColor = Color.White,
            content = content,
        )
    }
}