package com.jefisu.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.model.Currency
import com.jefisu.domain.repository.SettingsRepository
import com.jefisu.domain.repository.UserRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.ui.MessageController
import com.jefisu.ui.UiEventController
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.Navigator
import com.jefisu.ui.util.asMessageText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = combine(
        _state,
        settingsRepository.settings,
        userRepository.user,
    ) { state, settings, user ->
        state.copy(
            user = user,
            settings = settings,
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
            is SettingsAction.CurrencyChanged -> updateCurrency(action.currency)
            is SettingsAction.Navigate -> {
                viewModelScope.launch { navigator.navigate(action.destination) }
            }

            SettingsAction.NavigateBack -> {
                viewModelScope.launch { navigator.navigateUp() }
            }

            is SettingsAction.PasswordChanged -> {
                _state.update {
                    it.copy(
                        userPassword = action.password,
                        canDeleteAccount = action.password.isNotBlank(),
                    )
                }
            }

            SettingsAction.DeleteAccount -> deleteAccount()
        }
    }

    private fun toggleCloudSync() {
        viewModelScope.launch {
            settingsRepository.updateCloudSyncStatus()
        }
    }

    private fun updateLanguage(locale: Locale) {
        viewModelScope.launch {
            settingsRepository.updateLanguage(locale.language)
        }
    }

    private fun updateCurrency(currency: Currency) {
        viewModelScope.launch {
            settingsRepository.updateCurrency(currency)
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            launch { userRepository.signOut() }
            launch {
                navigator.navigate(Destination.AuthGraph) {
                    popUpTo(Destination.AuthenticatedGraph) {
                        inclusive = true
                    }
                }
            }
        }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            _state.update { it.copy(deleteAccountInProgress = true) }
            val result = userRepository.deleteAccount(_state.value.userPassword)
            result
                .onSuccess {
                    UiEventController.sendEvent(SettingsEvent.DeletedAccount)
                    navigator.navigate(Destination.AuthGraph) {
                        popUpTo(Destination.AuthenticatedGraph) {
                            inclusive = true
                        }
                    }
                }
                .onError { message ->
                    MessageController.sendMessage(message.asMessageText())
                }
            _state.update { it.copy(deleteAccountInProgress = false) }
        }
    }
}
