package com.jefisu.data.repository

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Settings
import com.jefisu.domain.model.getCurrency
import com.jefisu.domain.repository.SettingsRepository
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SettingsRepositoryImpl(
    private val context: Context,
    private val dataStore: DataStore<Preferences>,
    private val dispatcherProvider: DispatcherProvider,
) : SettingsRepository {

    private val _scope = CoroutineScope(dispatcherProvider.io + SupervisorJob())
    private val _settingsKey = stringPreferencesKey("settings")

    override val settings: StateFlow<Settings> = dataStore.data
        .transform {
            val settings = Json.decodeFromString<Settings>(it[_settingsKey] ?: return@transform)
            emit(settings)
        }
        .stateIn(
            _scope,
            SharingStarted.Eagerly,
            Settings(),
        )

    override suspend fun setDefaultSettings() {
        if (dataStore.data.first()[_settingsKey] == null) {
            dataStore.edit {
                it[_settingsKey] = Json.encodeToString(settings.value)
            }
        }
    }

    override suspend fun updateCloudSyncStatus() {
        updateSettings(
            settings.value.copy(isCloudSyncEnabled = !settings.value.isCloudSyncEnabled),
        )
    }

    override suspend fun updateCurrency(countryCode: String) {
        val locale = Locale("", countryCode)
        updateSettings(
            settings.value.copy(currency = locale.getCurrency()),
        )
    }

    override suspend fun updateLanguage(languageTag: String) {
        updateSettings(
            settings.value.copy(languageTag = languageTag),
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(LocaleManager::class.java)
            localeManager.applicationLocales = LocaleList.forLanguageTags(languageTag)
        } else {
            val appLocale = LocaleListCompat.forLanguageTags(languageTag)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }

    private suspend fun updateSettings(settings: Settings) {
        dataStore.edit { it[_settingsKey] = Json.encodeToString(settings) }
    }
}
