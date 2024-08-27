package com.jefisu.designsystem.util

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.formatMonthName(): String = this.month.name
    .lowercase()
    .replaceFirstChar { it.titlecase() }

fun LocalDate.toDateFormat(pattern: String = "MM.dd.yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return formatter.format(this)
}

@SuppressLint("DefaultLocale")
fun LocalDate.formatExpirationDate(): String {
    val pattern = "%02d"
    val month = pattern.format(monthValue)
    val year = pattern.format(year % 100)
    return "$month/$year"
}
