@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.spending_budgets.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.TrackizerAlertBottomSheet
import com.jefisu.designsystem.components.TrackizerBottomNavigation
import com.jefisu.designsystem.components.TrackizerOutlinedButton
import com.jefisu.designsystem.components.TrackizerScaffold
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.asColor
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.spending_budgets.R
import com.jefisu.spending_budgets.presentation.components.AddCategoryBottomSheet
import com.jefisu.spending_budgets.presentation.components.BudgetGauge
import com.jefisu.spending_budgets.presentation.components.CategoryItem
import com.jefisu.spending_budgets.presentation.components.PieData
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.util.SampleData
import com.jefisu.ui.R as UiRes

@Composable
internal fun SpendingBudgetScreen(
    state: SpendingBudgetsState,
    onAction: (SpendingBudgetsAction) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
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
    val lazyListState = rememberLazyListState()
    val showActionIcon by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 2
        }
    }

    val deleteSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    TrackizerAlertBottomSheet(
        sheetState = deleteSheetState,
        title = stringResource(
            id = UiRes.string.delete_alert_title,
            stringResource(UiRes.string.category).lowercase()
        ),
        description = stringResource(
            id = UiRes.string.delete_alert_description,
            stringResource(UiRes.string.category).lowercase()
        ),
        onDismissTextButton = stringResource(UiRes.string.button_alert_cancel),
        onConfirmTextButton = stringResource(UiRes.string.button_alert_delete),
        onConfirm = {
            onAction(SpendingBudgetsAction.DeleteCategory)
        },
        onDismiss = {
            onAction(SpendingBudgetsAction.ToogleDeleteAlert())
        },
    )

    val categorySheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    AddCategoryBottomSheet(
        sheetState = categorySheetState,
        state = state,
        onAction = onAction,
    )

    TrackizerScaffold(
        topBar = {
            TrackizerTopBar(
                title = stringResource(R.string.title_screen),
                scrollBehavior = scrollBehavior,
                actions = {
                    TrackizerTopBarDefaults.settingsActionIcon(
                        onClick = {
                            onAction(SpendingBudgetsAction.Navigate(Destination.SettingsScreen))
                        },
                    )
                    AnimatedVisibility(visible = showActionIcon) {
                        IconButton(
                            onClick = {
                                onAction(SpendingBudgetsAction.ToggleAddCategoryBottomSheet())
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AddCircleOutline,
                                contentDescription = stringResource(R.string.add_new_category),
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            state = lazyListState,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 78.dp),
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(innerPadding)
                .padding(horizontal = TrackizerTheme.spacing.extraMedium),
        ) {
            item {
                BudgetGauge(
                    budget = budget,
                    pieDataPoints = pieDataPoints,
                    modifier = Modifier.padding(top = 24.dp),
                )
            }
            item {
                Text(
                    text = stringResource(R.string.your_budgets_are_on_track, "\uD83D\uDC4D"),
                    style = TrackizerTheme.typography.headline2,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(
                            top = 40.dp,
                            bottom = TrackizerTheme.spacing.medium,
                        )
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Gray60,
                            shape = Shapes().large,
                        )
                        .padding(vertical = 24.dp),
                )
            }
            item {
                TrackizerOutlinedButton(
                    text = stringResource(R.string.add_new_category),
                    contentPadding = PaddingValues(TrackizerTheme.spacing.large),
                    onClick = {
                        onAction(SpendingBudgetsAction.ToggleAddCategoryBottomSheet())
                        categorySheetState.currentDetent = SheetDetent.FullyExpanded
                    },
                    modifier = Modifier.padding(bottom = TrackizerTheme.spacing.small),
                )
            }
            items(
                items = state.categories,
                key = { it.id },
            ) { category ->
                CategoryItem(
                    category = category,
                    modifier = Modifier
                        .padding(bottom = TrackizerTheme.spacing.small)
                        .rippleClickable(
                            onClick = {
                                onAction(
                                    SpendingBudgetsAction.ToggleAddCategoryBottomSheet(category),
                                )
                                categorySheetState.currentDetent = SheetDetent.FullyExpanded
                            },
                            onLongClick = {
                                onAction(SpendingBudgetsAction.ToogleDeleteAlert(category))
                                deleteSheetState.currentDetent = SheetDetent.FullyExpanded
                            },
                        ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SpendingBudgetScreenPreview() {
    TrackizerTheme {
        TrackizerBottomNavigation {
            SpendingBudgetScreen(
                state = SpendingBudgetsState(
                    categories = SampleData.categories,
                ),
                onAction = {},
            )
        }
    }
}
