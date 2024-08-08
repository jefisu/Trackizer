package com.jefisu.spending_budgets.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.CategoryType
import com.jefisu.spending_budgets.R
import com.jefisu.spending_budgets.presentation.components.AddCategory
import com.jefisu.spending_budgets.presentation.components.CategoriesList
import com.jefisu.spending_budgets.presentation.components.ChartHalfCircle
import com.jefisu.spending_budgets.presentation.components.PieData
import com.jefisu.ui.screen.BottomNavigationBody
import com.jefisu.ui.screen.BottomNavigationScreen
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray60
import com.jefisu.ui.theme.Theme
import com.jefisu.ui.util.asColor

@Composable
fun SpendingBudgetScreen(
    state: SpendingBudgetsState = SpendingBudgetsState(),
    onNavigateToSettings: () -> Unit = {},
) {
    val budget = remember(state.categories) {
        state.categories
            .sumOf { it.budget.toDouble() }
            .toFloat()
    }
    val pieDataPoints = remember(state.categories) {
        state.categories.map {
            PieData(
                value = it.usedBudget,
                color = it.type.asColor(),
            )
        }
    }

    BottomNavigationBody(
        title = stringResource(R.string.spending_budgets_title),
        onSettingsClick = onNavigateToSettings,
    ) {
        ChartHalfCircle(
            budget = budget,
            pieDataPoints = pieDataPoints,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp)
                .statusBarsPadding(),
        )
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 244.dp, bottom = 120.dp)
                .padding(horizontal = Theme.spacing.extraMedium)
                .statusBarsPadding(),
        ) {
            Text(
                text = stringResource(R.string.your_budgets_are_on_track, "\uD83D\uDC4D"),
                style = Theme.typography.headline2,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Gray60,
                        shape = Shapes().large,
                    )
                    .padding(vertical = 24.dp),
            )
            Spacer(modifier = Modifier.height(Theme.spacing.medium))
            if (state.categories.isNotEmpty()) {
                CategoriesList(
                    categories = state.categories,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(Theme.spacing.medium))
            }
            AddCategory(
                onClick = { },
            )
        }
    }
}

@Preview
@Composable
private fun SpendingBudgetScreenPreview() {
    val categories = listOf(
        Category(
            id = "1",
            type = CategoryType.Transport,
            budget = 100f,
            usedBudget = 80f,
            name = "Auto & Transport",
        ),
        Category(
            id = "2",
            type = CategoryType.Entertainment,
            budget = 200f,
            usedBudget = 33.5f,
            name = "Entertaiment",
        ),
        Category(
            id = "3",
            type = CategoryType.Security,
            budget = 40f,
            usedBudget = 10.99f,
            name = "Security",
        ),
    )

    AppTheme {
        BottomNavigationScreen {
            SpendingBudgetScreen(
                state = SpendingBudgetsState(
                    categories = categories,
                ),
            )
        }
    }
}
