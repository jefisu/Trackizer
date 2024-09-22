package com.jefisu.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.SettingsRepository
import com.jefisu.domain.repository.UserRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.ui.MessageController
import com.jefisu.ui.UiEventController
import com.jefisu.ui.asMessageText
import com.jefisu.ui.event.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = combine(
        _state,
        userRepository.user,
    ) { state, user ->
        state.copy(
            user = user,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5.seconds),
        _state.value,
    )

    fun onAction(action: SettingsAction) {
        when (action) {
            SettingsAction.EditProfile -> {
                viewModelScope.launch {
                    MessageController.sendMessage(
                        DataMessage.FUNCTIONALITY_UNAVAILABLE.asMessageText(),
                    )
                }
            }

            SettingsAction.ToggleCloudSync -> toggleCloudSync()
            is SettingsAction.SignOut -> signOut()
            is SettingsAction.LanguageChanged -> updateLanguage(action.locale)
            SettingsAction.ToogleLanguagePicker -> toggleLanguagePicker()
            is SettingsAction.CurrencyChanged -> updateCurrency(action.currencyCode)
            SettingsAction.ToogleCurrencyPicker -> toggleCurrencyPicker()
        }
    }

    private fun toggleCloudSync() {
        viewModelScope.launch {
            settingsRepository.updateCloudSyncStatus()
        }
    }

    private fun toggleLanguagePicker() {
        _state.update {
            it.copy(
                isLanguagePickerVisible = !it.isLanguagePickerVisible,
            )
        }
    }

    private fun toggleCurrencyPicker() {
        _state.update {
            it.copy(
                isCurrencyPickerVisible = !it.isCurrencyPickerVisible,
            )
        }
    }

    private fun updateLanguage(locale: Locale) {
        viewModelScope.launch {
            settingsRepository.updateLanguage(locale.language)
        }
    }

    private fun updateCurrency(countryCode: String) {
        viewModelScope.launch {
            settingsRepository.updateCurrency(countryCode)
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            userRepository.signOut()
            UiEventController.sendEvent(NavigationEvent.NavigateToWelcome)
        }
    }
}
