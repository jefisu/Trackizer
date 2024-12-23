package com.jefisu.calendar.presentation

import com.jefisu.ui.navigation.Destination
import java.time.LocalDate
import java.time.Month

sealed interface CalendarAction {
    data class SelectMonth(val month: Month) : CalendarAction
    data class SelectDay(val localDate: LocalDate) : CalendarAction
    data class Navigate(val destination: Destination) : CalendarAction
}