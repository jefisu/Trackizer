package com.jefisu.domain.model

import java.util.Locale
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val isCloudSyncEnabled: Boolean = true,
    val languageTag: String = "en-US",
    val currency: Currency = Locale.US.getCurrency(),
)
