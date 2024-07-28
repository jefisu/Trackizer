package com.jefisu.home.presentation.util

import java.text.NumberFormat

fun formatCurrency(value: Double): String {
    val currency = NumberFormat.getCurrencyInstance()
    return currency.format(value)
}
