package com.jefisu.ui.ext

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun LocalDate.formatMonthName(locale: Locale = Locale.getDefault()): String = this.month
    .getDisplayName(TextStyle.FULL, locale)
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
