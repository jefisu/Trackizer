package com.jefisu.trackizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.DataSyncRepository
import com.jefisu.domain.repository.SettingsRepository
import com.jefisu.ui.MessageController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val dataSyncRepository: DataSyncRepository,
) : ViewModel() {

    val state = combine(
        settingsRepository.settings,
        MessageController.message,
    ) { settings, message ->
        AppState(
            settings = settings,
            message = message,
        )
    }.onStart {
        viewModelScope.launch {
            dataSyncRepository.observeDataStoreChanges()
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        AppState(),
    )

    fun closeMessage() {
        MessageController.closeMessage()
    }
}
