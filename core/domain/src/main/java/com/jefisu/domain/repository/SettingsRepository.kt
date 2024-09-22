package com.jefisu.domain.repository

import com.jefisu.domain.model.Settings
import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settings: StateFlow<Settings>
    suspend fun setDefaultSettings()
    suspend fun updateCloudSyncStatus()
    suspend fun updateLanguage(languageTag: String)
    suspend fun updateCurrency(countryCode: String)
}
