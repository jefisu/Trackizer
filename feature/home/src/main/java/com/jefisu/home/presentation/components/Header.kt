package com.jefisu.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.home.R
import com.jefisu.home.presentation.HomeState
import com.jefisu.home.presentation.util.formatCurrency
import com.jefisu.ui.R as CommonRes
import com.jefisu.ui.components.ButtonProperties
import com.jefisu.ui.components.ButtonSize
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.StandardButton
import com.jefisu.ui.theme.AccentPrimary100
import com.jefisu.ui.theme.AccentPrimary50
import com.jefisu.ui.theme.AccentSecondary50
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Black18
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Gray40
import com.jefisu.ui.theme.Gray60
import com.jefisu.ui.theme.Gray70
import com.jefisu.ui.theme.Primary10
import com.jefisu.ui.theme.Purple90
import com.jefisu.ui.theme.Theme
import java.time.LocalDate

private const val START_ANGLE = 135f
private const val SWEEP_ANGLE = 270f

@Composable
internal fun Header(
    state: HomeState,
    onSettingsClick: () -> Unit,
    onSeeBudgetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val subscriptions = state.subscriptions
    val monthlyBudget = state.monthlyBudget

    val monthlyBillsValue by remember {
        derivedStateOf {
            val today = LocalDate.now()
            val items =
                subscriptions.filter { it.reminder || it.firstPaymentDate.month == today.month }
            if (items.isNotEmpty()) {
                0.0
            } else {
                items.sumOf { it.price.toDouble() }
            }
        }
    }
    val budgetCommittedPercentage: Float by remember {
        derivedStateOf {
            val result = monthlyBillsValue.toFloat() / monthlyBudget
            if (result.isNaN()) 0f else result
        }
    }

    val activeSubs: Int by remember {
        derivedStateOf {
            subscriptions.count { it.reminder }
        }
    }
    val highestSubs: String by remember {
        derivedStateOf {
            val highestValue = subscriptions.maxOfOrNull { it.price } ?: 0f
            formatCurrency(highestValue.toDouble())
        }
    }
    val lowestSubs: String by remember {
        derivedStateOf {
            val lowestValue = subscriptions.minOfOrNull { it.price } ?: 0f
            formatCurrency(lowestValue.toDouble())
        }
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp))
            .background(Gray70)
            .statusBarsPadding()
            .fillMaxWidth()
            .height(459.dp),
    ) {
        ArcProgress(progress = { budgetCommittedPercentage })

        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Theme.spacing.medium),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings icon",
                tint = Gray30,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 133.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = CommonRes.drawable.app_logo_no_background),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(Theme.spacing.small))
                Text(
                    text = stringResource(id = CommonRes.string.app_name).uppercase(),
                    style = Theme.typography.headline3,
                )
            }
            Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
            Text(
                text = formatCurrency(monthlyBillsValue),
                style = Theme.typography.headline7,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(205.dp),
            )
            Spacer(modifier = Modifier.height(Theme.spacing.medium))
            Text(
                text = stringResource(R.string.this_month_bills),
                style = Theme.typography.headline1,
                color = Gray40,
            )
            Spacer(modifier = Modifier.height(36.dp))
            StandardButton(
                text = stringResource(R.string.see_your_budget),
                properties = ButtonProperties(
                    type = ButtonType.Secondary,
                    size = ButtonSize.Small,
                ),
                onClick = onSeeBudgetClick,
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Theme.spacing.extraMedium),
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

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun HomeHeaderPreview() {
    AppTheme {
        Header(
            state = HomeState(),
            onSettingsClick = { },
            onSeeBudgetClick = { },
        )
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

        fun drawBlur() {
            val maxBlurArcs = 20
            for (i in 0..maxBlurArcs) {
                drawArc(
                    color = color.copy(alpha = i / 900f),
                    startAngle = startAngle,
                    sweepAngle = sweepAngle * progress,
                    useCenter = useCenter,
                    style = Stroke(
                        width = 80f + (maxBlurArcs - i) * 10,
                        cap = strokeCap,
                    ),
                )
            }
        }

        drawStrokeArc(thickness = thickness)
        drawBlur()
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
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Black18.copy(0f),
                            0.61f to Black18,
                        ),
                    ),
                ),
        )
        Canvas(
            modifier = Modifier.aspectRatio(1f),
        ) {
            val extraSmallArcScale = 0.7f
            scale(extraSmallArcScale, extraSmallArcScale) {
                drawArcProgress(
                    progress = progress(),
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
fun SubscriptionInfoItem(
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
                vertical = Theme.spacing.medium,
                horizontal = 18.dp,
            ),
    ) {
        Text(
            text = title,
            style = Theme.typography.headline1,
            color = Gray40,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = Theme.typography.headline2,
        )
    }
}
