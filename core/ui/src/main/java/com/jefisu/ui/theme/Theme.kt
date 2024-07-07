package com.jefisu.ui.theme

import android.app.Activity
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

object Theme {
    val size = Size()
    val typography = Typography()
    val spacing = Spacing()
}

@Composable
fun AppTheme(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Gray80,
    contentColor: Color = Color.White,
    isDarkIcons: Boolean = true,
    content: @Composable () -> Unit,
) {
    SystemBarColor(
        color = backgroundColor,
        isDarkIcons = isDarkIcons,
    )

    Surface(
        contentColor = contentColor,
        color = backgroundColor,
        modifier = modifier.systemBarsPadding(),
    ) {
        ProvideTextStyle(
            value = Theme.typography.bodyLarge,
            content = content,
        )
    }
}

@Composable
internal fun SystemBarColor(
    color: Color,
    isDarkIcons: Boolean,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.apply {
                statusBarColor = color.toArgb()
                navigationBarColor = statusBarColor
            }
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkIcons
            }
        }
    }
}
