package com.jefisu.home.presentation.util

import java.text.NumberFormat

fun formatCurrency(
    value: Double,
    isLiteralValue: Boolean = false,
): String {
    val numberFormat = NumberFormat.getCurrencyInstance()

    if (!isLiteralValue) {
        listOf(
            1e9 to "B",
            1e6 to "M",
            1e3 to "K",
        ).forEach { (floor, symbol) ->
            if (value >= floor) {
                val formattedValue = numberFormat
                    .format(value / floor)
                    .dropLast(3)
                return "$formattedValue$symbol"
            }
        }
    }

    return numberFormat.format(value)
}
