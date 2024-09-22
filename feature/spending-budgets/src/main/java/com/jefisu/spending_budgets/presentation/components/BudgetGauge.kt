package com.jefisu.spending_budgets.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.AccentSecondary100
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Primary10
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.drawBlur
import com.jefisu.designsystem.util.formatCurrency
import com.jefisu.spending_budgets.R

@Composable
internal fun BudgetGauge(
    budget: Float,
    pieDataPoints: List<PieData>,
    modifier: Modifier = Modifier,
) {
    val usedBudget = pieDataPoints.sumOf { it.value.toDouble() }

    Box(modifier = modifier) {
        SemiCircleChart(
            maxValue = with(usedBudget.toFloat()) { if (this > budget) this else budget },
            pieDataPoints = pieDataPoints,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 59.dp)
                .widthIn(max = 150.dp),
        ) {
            Text(
                text = formatCurrency(usedBudget),
                style = TrackizerTheme.typography.headline5,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(
                    R.string.of_budget,
                    formatCurrency(budget.toDouble()),
                ),
                style = TrackizerTheme.typography.bodySmall,
                color = Gray30,
            )
        }
    }
}

@Composable
private fun SemiCircleChart(
    maxValue: Float,
    pieDataPoints: List<PieData>,
    modifier: Modifier = Modifier,
    startAngle: Float = 180f,
    sweepAngle: Float = 180f,
    gapDegreesDp: Dp = 3.dp,
    strokeWidthBackground: Dp = 6.dp,
    strokeWidth: Dp = 10.dp,
    size: Dp = 206.dp,
) {
    val gapDegrees = with(LocalDensity.current) { gapDegreesDp.toPx() }
    val numberOfGaps = pieDataPoints.size - 1
    val remainingDegrees = sweepAngle - (gapDegrees * numberOfGaps)
    val usedValue = maxValue / remainingDegrees

    Canvas(
        modifier = modifier
            .width(size)
            .height(size / 2),
    ) {
        val arcSize = with(this.size) { copy(height = height * 2) }

        var currentSum = 0f
        val firstItemWithValueIndex = pieDataPoints.indexOfFirst { it.value > 0f }
        val arcs = pieDataPoints.mapIndexed { index, pieChartData ->
            val gap = if (index == firstItemWithValueIndex) 0f else gapDegrees
            val targetStartAngle = startAngle + currentSum + (index * gap)
            val targetSweepAngle = pieChartData.value / usedValue
            currentSum += targetSweepAngle
            ArcData(
                startAngle = targetStartAngle,
                targetSweepAngle = targetSweepAngle,
                color = pieChartData.color,
            )
        }

        drawArc(
            color = Gray60,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(
                width = strokeWidthBackground.toPx(),
                cap = StrokeCap.Round,
            ),
            size = arcSize,
        )
        arcs.forEach { arcData ->
            drawBlur(
                color = arcData.color,
                startAngle = arcData.startAngle,
                sweepAngle = arcData.targetSweepAngle,
                useCenter = false,
                maxBlurArcs = 10,
                size = arcSize,
            )

            drawArc(
                color = arcData.color,
                startAngle = arcData.startAngle,
                sweepAngle = arcData.targetSweepAngle,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth.toPx(),
                    cap = StrokeCap.Round,
                ),
                size = arcSize,
            )
        }
    }
}

internal data class PieData(val value: Float, val color: Color)

private data class ArcData(val targetSweepAngle: Float, val startAngle: Float, val color: Color)

@Preview
@Composable
private fun BudgetGaugePreview() {
    TrackizerTheme {
        BudgetGauge(
            budget = 100f,
            pieDataPoints = listOf(
                PieData(10f, AccentSecondary100),
                PieData(30f, AccentPrimary100),
                PieData(20f, Primary10),
            ),
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        )
    }
}
