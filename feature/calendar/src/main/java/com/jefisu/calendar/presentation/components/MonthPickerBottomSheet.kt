@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.calendar.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jefisu.calendar.R
import com.jefisu.calendar.presentation.CalendarAction
import com.jefisu.calendar.presentation.CalendarState
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerPicker
import com.jefisu.designsystem.components.TrackizerPickerDefaults
import com.jefisu.designsystem.components.hideSheet
import com.jefisu.designsystem.components.rememberPickerState
import com.jefisu.designsystem.spacing
import java.time.Month

@Composable
internal fun MonthPickerBottomSheet(
    state: CalendarState,
    onAction: (CalendarAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val months = Month.entries.map { it.name }
    val pickerState = rememberPickerState<String>()

    TrackizerBottomSheet(
        isVisible = state.showMonthPicker,
        sheetState = sheetState,
        onDismiss = { onAction(CalendarAction.ToggleMonthPicker) },
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = TrackizerTheme.spacing.extraMedium)
                .padding(horizontal = TrackizerTheme.spacing.extraMedium),
        ) {
            TrackizerPicker(
                items = months,
                visibleItemsCount = 5,
                state = pickerState,
                startIndex = months.indexOf(state.selectedMonth.month.name),
            ) { month ->
                TrackizerPickerDefaults.TextPickerItem(text = month)
            }
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.small))
            TrackizerButton(
                text = stringResource(R.string.apply),
                type = ButtonType.Primary,
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val month = Month.valueOf(pickerState.selectedItem.orEmpty())
                    onAction(CalendarAction.SelectMonth(month))
                    sheetState.hideSheet(
                        scope = scope,
                        onDismiss = { onAction(CalendarAction.ToggleMonthPicker) },
                    )
                },
            )
        }
    }
}
