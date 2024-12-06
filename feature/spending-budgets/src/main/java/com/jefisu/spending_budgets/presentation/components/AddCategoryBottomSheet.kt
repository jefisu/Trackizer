@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.spending_budgets.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.CurrencyTextField
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerOptionPicker
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.asIconResource
import com.jefisu.designsystem.util.asNameResource
import com.jefisu.domain.model.CategoryType
import com.jefisu.spending_budgets.R
import com.jefisu.spending_budgets.presentation.SpendingBudgetsAction
import com.jefisu.spending_budgets.presentation.SpendingBudgetsEvent
import com.jefisu.spending_budgets.presentation.SpendingBudgetsState
import com.jefisu.spending_budgets.presentation.util.CategoryDefaults
import com.jefisu.ui.ObserveAsEvents
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.UiEventController

@Composable
fun AddCategoryBottomSheet(
    sheetState: ModalBottomSheetState,
    state: SpendingBudgetsState,
    onAction: (SpendingBudgetsAction) -> Unit,
) {
    ObserveAsEvents(UiEventController.events) { event ->
        if (event is SpendingBudgetsEvent.HideAddCategoryBottomSheet) {
            sheetState.currentDetent = SheetDetent.Hidden
        }
    }

    TrackizerBottomSheet(
        sheetState = sheetState,
        onDismiss = { onAction(SpendingBudgetsAction.ToggleAddCategoryBottomSheet()) },
        modifier = Modifier.imePadding(),
    ) {
        TrackizerTextField(
            text = state.categoryName,
            onTextChange = {
                if (it.length <= CategoryDefaults.NAME_LENGTH) {
                    onAction(SpendingBudgetsAction.CategoryNameChanged(it))
                }
            },
            fieldName = stringResource(UiRes.string.category_name),
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
            label = stringResource(UiRes.string.budget),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
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
    val categoriesType = remember { CategoryType.entries }

    val categoryTypeSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    TrackizerOptionPicker(
        sheetState = categoryTypeSheetState,
        title = stringResource(R.string.select_a_category_type),
        items = categoriesType,
        startIndex = categoriesType.indexOf(state.categoryType),
        onDismiss = {},
        onSelectClick = {
            onAction(SpendingBudgetsAction.CategorTypeChanged(it))
        },
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
                text = stringResource(it.asNameResource()),
                style = TrackizerTheme.typography.headline4,
                color = Color.White,
            )
        }
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
                    onAction(SpendingBudgetsAction.CategorTypeChanged(state.categoryType))
                    categoryTypeSheetState.currentDetent = SheetDetent.FullyExpanded
                },
            ),
        ) {
            Text(
                text = stringResource(state.categoryType.asNameResource()),
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
