@file:OptIn(ExperimentalFoundationApi::class)

package com.jefisu.designsystem.util

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

fun Modifier.dropShadow(
    shape: Shape,
    color: Color = Color.Black.copy(0.25f),
    blur: Dp = 4.dp,
    offsetY: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp,
) = this.drawBehind {
    val shadowSize = Size(size.width + spread.toPx(), size.height + spread.toPx())
    val shadowOutline = shape.createOutline(shadowSize, layoutDirection, this)
    val paint =
        Paint().apply {
            this.color = color
        }

    if (blur.toPx() > 0) {
        paint.asFrameworkPaint().apply {
            maskFilter = BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
    }

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(offsetX.toPx(), offsetY.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}

fun DrawScope.drawBlur(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    useCenter: Boolean,
    maxBlurArcs: Int = 20,
    cap: StrokeCap = StrokeCap.Round,
    size: Size = this.size,
) {
    for (i in 0..maxBlurArcs) {
        drawArc(
            color = color.copy(alpha = i / 900f),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = useCenter,
            style = Stroke(
                width = 80f + (maxBlurArcs - i) * 10,
                cap = cap,
            ),
            size = size,
        )
    }
}

@Composable
fun Modifier.rippleClickable(
    enabled: Boolean = true,
    indication: Indication = rememberRipple(),
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) = combinedClickable(
    interactionSource = remember { MutableInteractionSource() },
    indication = indication,
    enabled = enabled,
    onClick = onClick,
    onLongClick = onLongClick,
)

fun Modifier.imeOffset(imeThresholdPercent: Float = 1f) = composed {
    val imePadding = WindowInsets.ime.asPaddingValues()
    offset {
        IntOffset.Zero.copy(
            y = -imePadding.calculateBottomPadding().times(imeThresholdPercent).roundToPx(),
        )
    }
}