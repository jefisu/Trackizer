package com.jefisu.calendar.presentation

import com.jefisu.domain.model.Subscription
import java.time.LocalDate

data class CalendarState(
    val selectedMonth: LocalDate = LocalDate.now(),
    val selectedDay: LocalDate = selectedMonth,
    val subscriptions: List<Subscription> = emptyList(),
)