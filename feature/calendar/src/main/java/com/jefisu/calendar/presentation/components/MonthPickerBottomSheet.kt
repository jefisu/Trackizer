package com.jefisu.calendar.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.composables.core.ModalBottomSheetState
import com.jefisu.calendar.R
import com.jefisu.calendar.presentation.CalendarAction
import com.jefisu.calendar.presentation.CalendarState
import com.jefisu.designsystem.components.TrackizerOptionPicker
import com.jefisu.designsystem.components.TrackizerPickerDefaults
import com.jefisu.designsystem.util.LocalSettings
import com.jefisu.ui.ext.formatMonthName
import java.time.LocalDate
import java.time.Month

@Composable
internal fun MonthPickerBottomSheet(
    state: CalendarState,
    sheetState: ModalBottomSheetState,
    onAction: (CalendarAction) -> Unit,
) {
    val months = remember { Month.entries }
    val settings = LocalSettings.current

    TrackizerOptionPicker(
        sheetState = sheetState,
        title = stringResource(R.string.select_a_month),
        items = months,
        startIndex = months.indexOf(state.selectedMonth.month),
        onDismiss = { },
        onSelectClick = { onAction(CalendarAction.SelectMonth(it)) },
    ) { month ->
        val date = LocalDate.now().withMonth(month.value)
        TrackizerPickerDefaults.PickerItem(
            text = date.formatMonthName(settings.toLanguageLocale()),
        )
    }
}
