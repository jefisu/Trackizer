package com.jefisu.subscription_info.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Black23
import com.jefisu.designsystem.Gray100
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme

@Composable
fun CustomDivider(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(27.dp),
    ) {
        val colors = listOf(Black23, Gray70)
        colors.forEachIndexed { index, color ->
            val height = size.height / colors.size
            drawRect(
                color = color,
                size = size.copy(height = height),
                topLeft = Offset.Zero.copy(y = height * index),
            )
        }
        listOf(0f, size.width).forEach { offsetX ->
            drawCircle(
                color = Gray100,
                center = center.copy(x = offsetX),
            )
        }
        drawLine(
            color = Gray100,
            start = center.copy(x = 0f),
            end = center.copy(x = size.width),
            strokeWidth = 1.5.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 20f)),
        )
    }
}

@Preview
@Composable
private fun CustomDividerPreview() {
    TrackizerTheme {
        CustomDivider(
            modifier = Modifier.width(200.dp),
        )
    }
}