package com.jefisu.settings.presentation

import com.jefisu.domain.model.Currency
import com.jefisu.ui.navigation.Destination
import java.util.Locale

sealed interface SettingsAction {
    data object EditProfile : SettingsAction
    data object ToggleCloudSync : SettingsAction
    data object SignOut : SettingsAction
    data class LanguageChanged(val locale: Locale) : SettingsAction
    data class CurrencyChanged(val currency: Currency) : SettingsAction
    data class Navigate(val destination: Destination) : SettingsAction
    data class PasswordChanged(val password: String) : SettingsAction
    data object DeleteAccount : SettingsAction
    data object NavigateBack : SettingsAction
}
