package com.jefisu.calendar.presentation.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Purple90
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.rippleClickable
import java.time.LocalDate

@Composable
internal fun DayBadgeItem(
    localDate: LocalDate,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val dayOfMonth = "%02d".format(localDate.dayOfMonth)
    val dayOfWeek = localDate.dayOfWeek

    val cornerDp = 16.dp
    val updateTransition = updateTransition(selected, label = "selected")
    val opacityBackground by updateTransition.animateFloat(
        targetValueByState = { if (it) 1f else 0.2f },
        label = "opacityBackground",
    )
    val circleScale by updateTransition.animateFloat(
        targetValueByState = { if (it) 1f else 0f },
        label = "circleScale",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(48.dp)
            .height(103.dp)
            .clip(RoundedCornerShape(cornerDp))
            .rippleClickable {
                onClick()
            }
            .drawBehind {
                val (width, height) = size
                val cornerRadius = CornerRadius(cornerDp.toPx(), cornerDp.toPx())
                drawRoundRect(
                    color = Gray60.copy(alpha = opacityBackground),
                    cornerRadius = cornerRadius,
                )
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Purple90.copy(0.15f), Color.Transparent),
                        end = Offset(width * 0.5f, height),
                    ),
                    cornerRadius = cornerRadius,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                    ),
                )
            }
            .padding(top = TrackizerTheme.spacing.small),
    ) {
        Text(
            text = dayOfMonth,
            style = TrackizerTheme.typography.headline4,
        )
        Text(
            text = dayOfWeek.name
                .take(2)
                .lowercase()
                .replaceFirstChar { it.titlecase() },
            style = TrackizerTheme.typography.bodySmall,
            color = Gray30,
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraMedium))
        Canvas(modifier = Modifier.size(6.dp)) {
            scale(circleScale) {
                drawCircle(color = AccentPrimary100)
            }
        }
    }
}

private class DayBadgeItemPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(true, false)
}

@Preview
@Composable
private fun DayBadgeItemPreview(
    @PreviewParameter(DayBadgeItemPreviewParameterProvider::class) selected: Boolean,
) {
    TrackizerTheme {
        DayBadgeItem(
            localDate = LocalDate.now(),
            selected = selected,
            onClick = {},
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        )
    }
}
