package com.jefisu.settings.presentation

import com.jefisu.ui.navigation.Destination
import java.util.Locale

sealed interface SettingsAction {
    data object EditProfile : SettingsAction
    data object ToggleCloudSync : SettingsAction
    data object SignOut : SettingsAction
    data object ToogleLanguagePicker : SettingsAction
    data object ToogleCurrencyPicker : SettingsAction
    data class LanguageChanged(val locale: Locale) : SettingsAction
    data class CurrencyChanged(val currencyCode: String) : SettingsAction

    data class Navigate(val destination: Destination) : SettingsAction
    data object NavigateBack : SettingsAction
}
