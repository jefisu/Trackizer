@file:OptIn(ExperimentalLayoutApi::class)

package com.jefisu.designsystem.util

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography

internal object TrackizerTextFieldDefaults {

    @Composable
    fun DecorationBox(
        fieldName: String,
        isFocused: Boolean,
        modifier: Modifier = Modifier,
        activeColor: Color = Color.White,
        inactiveColor: Color = Gray50,
        fieldNameStyle: TextStyle = TrackizerTheme.typography.bodyMedium,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        textField: @Composable RowScope.() -> Unit,
    ) {
        val colorAnim by animateColorAsState(
            targetValue = if (isFocused) activeColor else inactiveColor,
            label = "TrackizerTextField",
        )

        ObserveKeyboard(isFocused)

        Column(
            horizontalAlignment = horizontalAlignment,
            modifier = modifier,
        ) {
            CompositionLocalProvider(
                LocalContentColor provides colorAnim,
                LocalTextStyle provides TrackizerTheme.typography.bodyLarge.copy(
                    color = colorAnim,
                ),
            ) {
                Text(
                    text = fieldName,
                    style = fieldNameStyle,
                    color = colorAnim,
                    fontWeight = FontWeight.Medium,
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.small))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(TrackizerTheme.size.textFieldHeight)
                        .drawBehind {
                            drawRoundRect(
                                color = colorAnim,
                                cornerRadius = CornerRadius(16.dp.toPx()),
                                style = Stroke(width = 1.dp.toPx()),
                            )
                        }
                        .padding(
                            start = TrackizerTheme.spacing.medium,
                            end = 4.dp,
                        ),
                ) {
                    textField()
                }
            }
        }
    }

    @Composable
    private fun ObserveKeyboard(isFocused: Boolean) {
        val focusManager = LocalFocusManager.current
        val isVisibleKeyboard = WindowInsets.isImeVisible
        LaunchedEffect(isVisibleKeyboard) {
            if (!isVisibleKeyboard && isFocused) focusManager.clearFocus()
        }
    }
}