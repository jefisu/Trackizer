package com.jefisu.designsystem.util

import androidx.compose.animation.Animatable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
        textField: @Composable RowScope.() -> Unit,
    ) {
        val colorAnim = remember { Animatable(inactiveColor) }
        LaunchedEffect(Unit) {
            snapshotFlow { isFocused }.collect {
                colorAnim.animateTo(if (it) activeColor else inactiveColor)
            }
        }

        CompositionLocalProvider(
            LocalContentColor provides colorAnim.value,
            LocalTextStyle provides TrackizerTheme.typography.bodyLarge.copy(
                color = colorAnim.value,
            ),
        ) {
            Column(modifier = modifier) {
                Text(
                    text = fieldName,
                    style = fieldNameStyle,
                    color = colorAnim.value,
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
                                color = colorAnim.value,
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
}
