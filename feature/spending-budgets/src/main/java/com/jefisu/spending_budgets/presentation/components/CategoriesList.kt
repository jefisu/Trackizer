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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType
import com.jefisu.spending_budgets.R
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Gray60
import com.jefisu.ui.theme.Purple90
import com.jefisu.ui.theme.Theme
import com.jefisu.ui.util.asColor
import com.jefisu.ui.util.asIconResource
import com.jefisu.ui.util.formatCurrency

@Composable
fun CategoriesList(
    categories: List<Category>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
        modifier = modifier,
    ) {
        items(
            items = categories,
            key = { it.id },
        ) { category ->
            CategoryItem(category = category)
        }
    }
}

@Composable
private fun CategoryItem(
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
                    cornerRadius = CornerRadius(corner, corner),
                )
                drawRoundRect(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0f to Purple90.copy(0.2f),
                            1f to Color.Transparent,
                        ),
                        end = Offset(
                            x = center.x,
                            y = size.height,
                        ),
                    ),
                    cornerRadius = CornerRadius(corner, corner),
                    style = Stroke(width = 1.dp.toPx()),
                )
            }
            .padding(Theme.spacing.medium),
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
                    style = Theme.typography.headline2,
                )
                Text(
                    text = stringResource(
                        R.string.left_to_spend,
                        formatCurrency(leftToSpend.toDouble()),
                    ),
                    style = Theme.typography.bodySmall,
                    color = Gray30,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = formatCurrency(usedBudget.toDouble()),
                    style = Theme.typography.headline2,
                )
                Text(
                    text = stringResource(R.string.of, formatCurrency(budget.toDouble())),
                    style = Theme.typography.bodySmall,
                    color = Gray30,
                )
            }
        }
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
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
                width = size.width * progress(),
            ),
        )
    }
}

@Preview
@Composable
private fun CategoriesListPreview() {
    AppTheme {
        CategoriesList(
            categories = listOf(
                Category(
                    id = "1",
                    type = CategoryType.Transport,
                    budget = 100f,
                    usedBudget = 80f,
                    name = "Auto & Transport",
                ),
            ),
            modifier = Modifier.padding(Theme.spacing.small),
        )
    }
}
