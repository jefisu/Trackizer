package com.jefisu.designsystem.util

import android.icu.text.CompactDecimalFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.text.NumberFormat

@Composable
fun formatCurrency(
    value: Double,
    isCompactFormat: Boolean = true,
): String {
    val settings = LocalAppConfig.current.settings
    return remember(value, settings.currency) {
        val locale = settings.currency.toLocale()
        val numberFormat = NumberFormat.getCurrencyInstance(locale)

        val floors = listOf(1e9, 1e6, 1e3)
        if (isCompactFormat && floors.any { value >= it }) {
            val compactDecimalFormat = CompactDecimalFormat.getInstance(
                locale,
                CompactDecimalFormat.CompactStyle.SHORT,
            )
            val formattedValue = compactDecimalFormat.format(value)
            val symbol = numberFormat.currency?.symbol?.removePrefix(locale.country)
            return@remember "$symbol $formattedValue"
        }

        return@remember numberFormat.format(value).removePrefix(locale.country)
    }
}
