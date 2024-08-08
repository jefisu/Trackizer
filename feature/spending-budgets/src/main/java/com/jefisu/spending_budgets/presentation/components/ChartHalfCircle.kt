package com.jefisu.spending_budgets.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.spending_budgets.R
import com.jefisu.ui.components.AnimatedText
import com.jefisu.ui.ext.draw_scope.drawArcBlur
import com.jefisu.ui.theme.AccentPrimary100
import com.jefisu.ui.theme.AccentSecondary100
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Gray60
import com.jefisu.ui.theme.Primary10
import com.jefisu.ui.theme.Theme
import com.jefisu.ui.util.formatCurrency

@Composable
fun ChartHalfCircle(
    budget: Float,
    pieDataPoints: List<PieData>,
    modifier: Modifier = Modifier,
) {
    val usedBudget = pieDataPoints.sumOf { it.value.toDouble() }

    Box(modifier = modifier) {
        AnimatedSemiCircleChart(
            maxValue = budget,
            pieDataPoints = pieDataPoints,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 59.dp)
                .widthIn(max = 150.dp),
        ) {
            AnimatedText(
                text = formatCurrency(usedBudget),
                style = Theme.typography.headline5,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.of_budget, formatCurrency(budget.toDouble())),
                style = Theme.typography.bodySmall,
                color = Gray30,
            )
        }
    }
}

@Composable
private fun AnimatedSemiCircleChart(
    maxValue: Float,
    pieDataPoints: List<PieData>,
    modifier: Modifier = Modifier,
    startAngle: Float = 180f,
    sweepAngle: Float = 180f,
    gapDegreesDp: Dp = 1.8.dp,
    strokeWidthBackground: Dp = 6.dp,
    strokeWidth: Dp = 10.dp,
) {
    val view = LocalView.current

    val gapDegrees =
        if (view.isInEditMode) 8f else with(LocalDensity.current) { gapDegreesDp.toPx() }
    val numberOfGaps = pieDataPoints.size - 1
    val remainingDegrees = sweepAngle - (gapDegrees * numberOfGaps)
    val usedValue = maxValue / remainingDegrees

    var currentSum = 0f
    val arcs = pieDataPoints.mapIndexed { index, pieChartData ->
        val targetStartAngle = startAngle + currentSum + (index * gapDegrees)
        val targetSweepAngle = pieChartData.value / usedValue
        currentSum += targetSweepAngle
        ArcData(
            startAngle = targetStartAngle,
            targetSweepAngle = targetSweepAngle,
            color = pieChartData.color,
        )
    }

    Canvas(modifier = modifier.size(206.dp)) {
        drawArc(
            color = Gray60,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(
                width = strokeWidthBackground.toPx(),
                cap = StrokeCap.Round,
            ),
        )
        arcs.forEach { arcData ->
            drawArcBlur(
                color = arcData.color,
                startAngle = arcData.startAngle,
                sweepAngle = arcData.targetSweepAngle,
                useCenter = false,
                maxBlurArcs = 10,
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
            )
        }
    }
}

data class PieData(val value: Float, val color: Color)

data class ArcData(val targetSweepAngle: Float, val startAngle: Float, val color: Color)

@Preview
@Composable
private fun ChartHalfCirclePreview() {
    AppTheme {
        ChartHalfCircle(
            budget = 100f,
            pieDataPoints = listOf(
                PieData(10f, AccentSecondary100),
                PieData(30f, AccentPrimary100),
                PieData(20f, Primary10),
            ),
            modifier = Modifier.padding(Theme.spacing.small),
        )
    }
}
