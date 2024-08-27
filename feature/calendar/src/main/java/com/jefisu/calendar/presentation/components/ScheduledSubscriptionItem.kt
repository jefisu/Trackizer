package com.jefisu.calendar.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Purple90
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.SubscriptionIcon
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.SampleData
import com.jefisu.designsystem.util.formatCurrency
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.domain.model.Subscription

@Composable
internal fun ScheduledSubscriptionItem(
    subscription: Subscription,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val cornerDp = 16.dp
    Column(
        modifier = modifier
            .sizeIn(
                minWidth = 160.dp,
                minHeight = 168.dp,
            )
            .clip(RoundedCornerShape(cornerDp))
            .rippleClickable {
                onClick()
            }
            .drawBehind {
                val (width, height) = size
                val cornerRadius = CornerRadius(cornerDp.toPx(), cornerDp.toPx())
                drawRoundRect(
                    color = Gray60.copy(0.2f),
                    cornerRadius = cornerRadius,
                )
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colors = listOf(Purple90.copy(0.15f), Color.Transparent),
                        end = Offset(width * 0.5f, height),
                    ),
                    cornerRadius = cornerRadius,
                    style = Stroke(width = 1.5.dp.toPx()),
                )
            }
            .padding(TrackizerTheme.spacing.medium),
    ) {
        SubscriptionIcon(icon = subscription.service)
        Spacer(modifier = Modifier.height(44.dp))
        Text(
            text = subscription.service.title,
            style = TrackizerTheme.typography.headline2,
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.small))
        Text(
            text = formatCurrency(subscription.price.toDouble()),
            style = TrackizerTheme.typography.headline4,
        )
    }
}

@Preview
@Composable
private fun ScheduledSubscriptionItemPreview() {
    TrackizerTheme {
        ScheduledSubscriptionItem(
            subscription = SampleData.subscriptions.first(),
            onClick = {},
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        )
    }
}
