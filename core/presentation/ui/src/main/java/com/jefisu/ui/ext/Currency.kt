package com.jefisu.ui.ext

import android.icu.text.CompactDecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(
    value: Double,
    isLongValue: Boolean = false,
): String {
    val numberFormat = NumberFormat.getCurrencyInstance()

    val floors = listOf(1e9, 1e6, 1e3)
    if (!isLongValue && floors.any { value >= it }) {
        val compactDecimalFormat = CompactDecimalFormat.getInstance(
            Locale.getDefault(),
            CompactDecimalFormat.CompactStyle.SHORT,
        )
        val formattedValue = compactDecimalFormat.format(value)
        return "${numberFormat.currency?.symbol} $formattedValue"
    }

    return numberFormat.format(value)
}
