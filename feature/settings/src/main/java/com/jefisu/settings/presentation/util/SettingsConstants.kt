package com.jefisu.settings.presentation.util

import com.jefisu.domain.model.getCurrency
import java.util.Locale

object SettingsConstants {
    val localesAvailable = listOf(
        Locale.forLanguageTag("en-US"),
        Locale.forLanguageTag("pt-BR"),
    )
    val currencys = localesAvailable.map { it.getCurrency() }
}
