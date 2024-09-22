package com.jefisu.calendar.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.jefisu.calendar.R
import com.jefisu.calendar.presentation.CalendarAction
import com.jefisu.calendar.presentation.CalendarState
import com.jefisu.designsystem.components.TrackizerOptionPicker
import com.jefisu.designsystem.components.TrackizerPickerDefaults
import java.time.Month

@Composable
internal fun MonthPickerBottomSheet(
    state: CalendarState,
    onAction: (CalendarAction) -> Unit,
) {
    val months = remember { Month.entries }

    TrackizerOptionPicker(
        title = stringResource(R.string.select_a_month),
        visible = state.showMonthPicker,
        items = months,
        startIndex = months.indexOf(state.selectedMonth.month),
        onDismiss = { onAction(CalendarAction.ToggleMonthPicker) },
        onSelectClick = { onAction(CalendarAction.SelectMonth(it)) },
    ) { month ->
        TrackizerPickerDefaults.PickerItem(text = month.name)
    }
}
