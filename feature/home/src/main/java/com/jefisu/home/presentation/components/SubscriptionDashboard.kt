package com.jefisu.home.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.AccentPrimary50
import com.jefisu.designsystem.AccentSecondary50
import com.jefisu.designsystem.Black18
import com.jefisu.designsystem.Gray40
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.Primary10
import com.jefisu.designsystem.Purple90
import com.jefisu.designsystem.R as UiRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.AnimatedText
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.drawBlur
import com.jefisu.designsystem.util.formatCurrency
import com.jefisu.home.R
import com.jefisu.home.presentation.HomeState
import java.time.LocalDate

private const val START_ANGLE = 135f
private const val SWEEP_ANGLE = 270f

@Composable
internal fun SubscriptionDashboard(
    state: HomeState,
    onSeeBudgetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val subscriptions = state.subscriptions
    val monthlyBudget = state.monthlyBudget

    val monthlyBillsValue = subscriptions
        .filter { it.reminder || it.firstPayment.month == LocalDate.now().month }
        .sumOf { it.price.toDouble() }

    val budgetCommittedPercentage by animateFloatAsState(
        targetValue = run {
            val budget = (if (monthlyBudget > 0) monthlyBudget else monthlyBillsValue).toFloat()
            val result = monthlyBillsValue.toFloat() / budget
            if (result.isNaN()) 0f else result.coerceAtMost(1f)
        },
        label = "",
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing,
        ),
    )

    val activeSubs = subscriptions.count { it.reminder }
    val highestSubs =
        formatCurrency(subscriptions.maxOfOrNull { it.price.toDouble() } ?: 0.0)
    val lowestSubs =
        formatCurrency(subscriptions.minOfOrNull { it.price.toDouble() } ?: 0.0)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp))
            .background(Gray70)
            .statusBarsPadding()
            .fillMaxWidth()
            .height(459.dp),
    ) {
        ArcProgress(progress = { budgetCommittedPercentage })

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 133.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = UiRes.drawable.app_logo_no_background),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(TrackizerTheme.spacing.small))
                Text(
                    text = stringResource(id = UiRes.string.app_name).uppercase(),
                    style = TrackizerTheme.typography.headline3,
                )
            }
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraMedium))
            AnimatedText(
                text = formatCurrency(monthlyBillsValue),
                style = TrackizerTheme.typography.headline7,
                modifier = Modifier.width(205.dp),
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
            Text(
                text = stringResource(R.string.this_month_bills),
                style = TrackizerTheme.typography.headline1,
                color = Gray40,
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.large))
            TrackizerButton(
                text = stringResource(R.string.see_your_budget),
                type = ButtonType.Secondary,
                onClick = onSeeBudgetClick,
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .height(TrackizerTheme.size.buttonHeightSmall)
                    .width(130.dp),
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(TrackizerTheme.spacing.extraMedium),
        ) {
            SubscriptionInfoItem(
                title = stringResource(R.string.active_subs),
                value = activeSubs.toString(),
                highlightedColor = AccentPrimary50,
                modifier = Modifier.weight(1f),
            )
            SubscriptionInfoItem(
                title = stringResource(R.string.highest_subs),
                value = highestSubs,
                highlightedColor = Primary10,
                modifier = Modifier.weight(1f),
            )
            SubscriptionInfoItem(
                title = stringResource(R.string.lowest_subs),
                value = lowestSubs,
                highlightedColor = AccentSecondary50,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ArcProgress(progress: () -> Float) {
    fun DrawScope.drawDashedArc(
        thickness: Float = 3.dp.toPx(),
        spacing: Float = 8.dp.toPx(),
        color: Color = Color.White,
        startAngle: Float = START_ANGLE,
        sweepAngle: Float = SWEEP_ANGLE,
    ) {
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(1f, spacing), 0f)
        val stroke = Stroke(
            width = thickness,
            pathEffect = pathEffect,
            cap = StrokeCap.Round,
        )
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = stroke,
        )
    }

    fun DrawScope.drawArcProgress(
        progress: Float,
        thickness: Float = 16.dp.toPx(),
        color: Color = AccentPrimary100,
        startAngle: Float = START_ANGLE,
        sweepAngle: Float = SWEEP_ANGLE,
    ) {
        val strokeCap = StrokeCap.Round
        val useCenter = false

        fun drawStrokeArc(thickness: Float) {
            drawArc(
                brush = Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Purple90.copy(0f),
                        0.3f to Purple90.copy(0f),
                        0.6f to Purple90.copy(0.5f),
                        1f to Purple90.copy(0.4f),
                    ),
                ),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = useCenter,
                style = Stroke(
                    width = thickness + 1.dp.toPx(),
                    cap = strokeCap,
                ),
            )
            drawArc(
                color = Gray60,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = useCenter,
                style = Stroke(
                    width = thickness,
                    cap = strokeCap,
                ),
            )
        }

        drawStrokeArc(thickness = thickness)
        drawBlur(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle * progress,
            useCenter = useCenter,
        )
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = sweepAngle * progress,
            useCenter = useCenter,
            style = Stroke(
                width = thickness,
                cap = StrokeCap.Round,
            ),
        )
    }

    val dropShadow: @Composable BoxScope.() -> Unit = {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    Brush.verticalGradient(
                        0f to Black18.copy(0f),
                        0.61f to Black18,
                    ),
                ),
        )
    }

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp),
    ) {
        Canvas(modifier = Modifier.aspectRatio(1f)) {
            drawDashedArc(
                color = Color.White.copy(0.03f),
                startAngle = 180f,
                sweepAngle = 180f,
            )

            val mediumArcScale = 0.85f
            scale(mediumArcScale, mediumArcScale) {
                drawDashedArc(color = Color.White.copy(0.05f))
            }
        }
        dropShadow()
        Canvas(
            modifier = Modifier.aspectRatio(1f),
        ) {
            val extraSmallArcScale = 0.7f
            scale(extraSmallArcScale, extraSmallArcScale) {
                drawArcProgress(
                    progress = progress().coerceAtMost(1f),
                    startAngle = START_ANGLE,
                    sweepAngle = SWEEP_ANGLE,
                )
            }

            val smallArcScale = 0.55f
            scale(smallArcScale, smallArcScale) {
                drawDashedArc(
                    color = Gray60.copy(0.5f),
                    spacing = 20.dp.toPx(),
                    thickness = 6.dp.toPx(),
                )
            }
        }
    }
}

@Composable
private fun SubscriptionInfoItem(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    highlightedColor: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .drawBehind {
                val radius = 18.dp.toPx()
                drawRoundRect(
                    color = Gray60.copy(0.2f),
                    cornerRadius = CornerRadius(radius, radius),
                )

                drawRoundRect(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Purple90.copy(0.15f),
                            1f to Color.Transparent,
                        ),
                        end = Offset(
                            x = size.width * 0.5f,
                            y = size.height,
                        ),
                    ),
                    cornerRadius = CornerRadius(radius, radius),
                    style = Stroke(width = 1.dp.toPx()),
                )

                drawLine(
                    color = highlightedColor,
                    start = Offset(
                        x = size.width * 0.3f,
                        y = 0f,
                    ),
                    end = Offset(
                        x = size.width * 0.7f,
                        y = 0f,
                    ),
                )
            }
            .padding(
                vertical = TrackizerTheme.spacing.medium,
                horizontal = 14.dp,
            ),
    ) {
        Text(
            text = title,
            style = TrackizerTheme.typography.headline1,
            color = Gray40,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(2.dp))
        AnimatedText(
            text = value,
            style = TrackizerTheme.typography.headline2,
        )
    }
}

@Preview
@Composable
private fun SubscriptionDashboardPreview() {
    TrackizerTheme {
        SubscriptionDashboard(
            state = HomeState(),
            onSeeBudgetClick = { },
        )
    }
}
