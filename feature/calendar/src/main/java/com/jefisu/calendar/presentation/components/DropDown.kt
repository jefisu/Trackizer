package com.jefisu.calendar.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.rippleClickable

@Composable
internal fun DropDown(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .rippleClickable {
                onClick()
            }
            .drawBehind {
                val (width, height) = size
                val corner = 16.dp.toPx()
                val cornerRadius = CornerRadius(corner, corner)

                drawRoundRect(
                    color = Color.White.copy(0.1f),
                    cornerRadius = cornerRadius,
                )
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.White.copy(0.15f), Color.Transparent),
                        start = Offset(width * 0.4f, 0f),
                        end = Offset(width * 0.5f, height),
                    ),
                    style = Stroke(width = 1.dp.toPx()),
                    cornerRadius = cornerRadius,
                )
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .animateContentSize(),
    ) {
        Text(
            text = text,
            style = TrackizerTheme.typography.headline1,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier.size(12.dp),
        )
    }
}

@Preview
@Composable
private fun DropDownPreview() {
    TrackizerTheme {
        DropDown(text = "January", onClick = {})
    }
}