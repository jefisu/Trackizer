package com.jefisu.spending_budgets.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.BorderBrush
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType
import com.jefisu.spending_budgets.R
import com.jefisu.spending_budgets.presentation.util.asColor
import com.jefisu.spending_budgets.presentation.util.asIconResource
import com.jefisu.ui.ext.formatCurrency
import kotlin.math.absoluteValue

@Composable
internal fun CategoryItem(
    category: Category,
    modifier: Modifier = Modifier,
) {
    val budget = category.budget
    val usedBudget = category.usedBudget
    val usedBudgetPercent = (usedBudget / budget).coerceAtMost(budget)
    val leftToSpend = budget - usedBudget

    Column(
        modifier = modifier
            .drawBehind {
                val corner = 16.dp.toPx()
                drawRoundRect(
                    color = Gray60.copy(0.2f),
                    cornerRadius = CornerRadius(corner),
                )
                drawRoundRect(
                    brush = BorderBrush,
                    cornerRadius = CornerRadius(corner),
                    style = Stroke(width = 1.dp.toPx()),
                )
            }
            .padding(TrackizerTheme.spacing.medium),
    ) {
        Row {
            Icon(
                painter = painterResource(category.type.asIconResource()),
                contentDescription = category.name,
                tint = Gray30,
                modifier = Modifier.size(32.dp),
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = category.name,
                    style = TrackizerTheme.typography.headline2,
                )
                Text(
                    text = stringResource(
                        if (leftToSpend < 0f) {
                            R.string.exceeded_budget
                        } else {
                            R.string.left_to_spend
                        },
                        formatCurrency(leftToSpend.toDouble().absoluteValue),
                    ),
                    style = TrackizerTheme.typography.bodySmall,
                    color = Gray30,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = formatCurrency(usedBudget.toDouble()),
                    style = TrackizerTheme.typography.headline2,
                )
                Text(
                    text = stringResource(R.string.of, formatCurrency(budget.toDouble())),
                    style = TrackizerTheme.typography.bodySmall,
                    color = Gray30,
                )
            }
        }
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
        ProgressBar(
            progress = { usedBudgetPercent },
            color = category.type.asColor(),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ProgressBar(
    progress: () -> Float,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(3.dp),
    ) {
        val corner = 16.dp.toPx()
        drawRoundRect(
            color = Gray60,
            cornerRadius = CornerRadius(corner, corner),
        )
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(corner, corner),
            size = size.copy(
                width = size.width * progress().coerceAtMost(1f),
            ),
        )
    }
}

@Preview
@Composable
private fun CategoryItemPreview() {
    TrackizerTheme {
        CategoryItem(
            category = Category(
                id = "1",
                type = CategoryType.Transport,
                budget = 100f,
                usedBudget = 80f,
                name = "Auto & Transport",
            ),
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        )
    }
}
