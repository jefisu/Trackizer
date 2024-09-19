@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.spending_budgets.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.CurrencyTextField
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerPicker
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.components.hideSheet
import com.jefisu.designsystem.components.rememberPickerState
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.domain.model.CategoryType
import com.jefisu.spending_budgets.R
import com.jefisu.spending_budgets.presentation.SpendingBudgetsAction
import com.jefisu.spending_budgets.presentation.SpendingBudgetsEvent
import com.jefisu.spending_budgets.presentation.SpendingBudgetsState
import com.jefisu.ui.ObserveAsEvents
import com.jefisu.ui.UiEventController
import com.jefisu.ui.util.asIconResource

@Composable
fun AddCategoryBottomSheet(
    state: SpendingBudgetsState,
    onAction: (SpendingBudgetsAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ObserveAsEvents(UiEventController.events) { event ->
        if (event is SpendingBudgetsEvent.HideAddCategoryBottomSheet) {
            sheetState.hideSheet(
                scope = scope,
                onDismiss = { onAction(SpendingBudgetsAction.ToggleAddCategoryBottomSheet()) },
            )
        }
    }

    TrackizerBottomSheet(
        sheetState = sheetState,
        isVisible = state.showAddCategoryBottomSheet,
        onDismiss = { onAction(SpendingBudgetsAction.ToggleAddCategoryBottomSheet()) },
    ) {
        TrackizerTextField(
            text = state.categoryName,
            onTextChange = { onAction(SpendingBudgetsAction.CategoryNameChanged(it)) },
            fieldName = "Category name",
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        CategoryTypePicker(
            state = state,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        CurrencyTextField(
            text = state.categoryBudget,
            onTextChange = { onAction(SpendingBudgetsAction.CategoryBudgetChanged(it)) },
            label = "Budget",
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.large))
        TrackizerButton(
            text = stringResource(
                state.category?.let { R.string.edit_category } ?: R.string.add_category,
            ),
            type = ButtonType.Primary,
            onClick = { onAction(SpendingBudgetsAction.AddCategory) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun CategoryTypePicker(
    state: SpendingBudgetsState,
    onAction: (SpendingBudgetsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val pickerState = rememberPickerState<CategoryType>()

    val categoriesType = remember { CategoryType.entries }

    TrackizerBottomSheet(
        isVisible = state.isSelectingCategoryType,
        sheetState = sheetState,
        onDismiss = {
            onAction(SpendingBudgetsAction.ToogleCategoryTypePicker)
        },
        horizontalAligment = Alignment.Start,
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.select_a_category_type),
            style = TrackizerTheme.typography.headline3,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.medium))
        TrackizerPicker(
            state = pickerState,
            items = categoriesType,
            startIndex = categoriesType.indexOf(state.categoryType),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(it.asIconResource()),
                    contentDescription = it.name,
                    tint = Color.White,
                )
                Spacer(Modifier.width(TrackizerTheme.spacing.small))
                Text(
                    text = it.name,
                    style = TrackizerTheme.typography.headline4,
                    color = Color.White,
                )
            }
        }
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        TrackizerButton(
            text = stringResource(R.string.select),
            type = ButtonType.Primary,
            onClick = {
                onAction(SpendingBudgetsAction.CategorTypeChanged(pickerState.selectedItem!!))
                sheetState.hideSheet(
                    scope = scope,
                    onDismiss = {
                        onAction(SpendingBudgetsAction.ToogleCategoryTypePicker)
                    },
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = TrackizerTheme.spacing.small),
    ) {
        Text(
            text = stringResource(R.string.category_type),
            style = TrackizerTheme.typography.bodyMedium,
            color = Gray50,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraSmall))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = null,
                onClick = {
                    listOf(
                        SpendingBudgetsAction.CategorTypeChanged(state.categoryType),
                        SpendingBudgetsAction.ToogleCategoryTypePicker,
                    ).forEach(onAction)
                },
            ),
        ) {
            Text(
                text = state.categoryType.name,
                style = TrackizerTheme.typography.headline4,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            Icon(
                painter = painterResource(DesignSystemRes.drawable.ic_back),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(TrackizerTheme.size.iconExtraSmall)
                    .rotate(180f),
            )
        }
        Spacer(Modifier.height(TrackizerTheme.spacing.extraSmall))
        HorizontalDivider(color = Gray70)
    }
}