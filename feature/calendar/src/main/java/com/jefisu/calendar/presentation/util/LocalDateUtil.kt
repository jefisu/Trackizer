package com.jefisu.calendar.presentation.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal fun LocalDate.formatMonthName(): String {
    return this.month.name
        .lowercase()
        .replaceFirstChar { it.titlecase() }
}

internal fun LocalDate.toDateFormat(
    pattern: String = "MM.dd.yyyy",
): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(this)
}