package com.jefisu.domain.model

import java.util.Locale
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val isCloudSyncEnabled: Boolean = true,
    val languageTag: String = Locale.getDefault().toLanguageTag(),
    val currency: Currency = Locale.forLanguageTag(languageTag).getCurrency(),
) {
    fun toLanguageLocale() = Locale(languageTag)
}
