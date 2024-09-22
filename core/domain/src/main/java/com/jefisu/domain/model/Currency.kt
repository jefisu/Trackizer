package com.jefisu.domain.model

import java.util.Currency as AndroidCurrency
import java.util.Locale
import kotlinx.serialization.Serializable

@Serializable
data class Currency(val code: String, val symbol: String, val country: String, val name: String) {
    fun displayCodeWithSymbol() = "$code ($symbol)"
    fun displayNameWithSymbol() = "$name ($symbol)"

    fun toLocale() = Locale("", country)
}

fun Locale.getCurrency(): Currency {
    val androidCurrency = AndroidCurrency.getInstance(this)
    return Currency(
        code = androidCurrency.currencyCode,
        symbol = androidCurrency.symbol.removePrefix(country),
        country = country,
        name = androidCurrency.displayName,
    )
}
