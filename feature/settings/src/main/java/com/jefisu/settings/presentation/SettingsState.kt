package com.jefisu.settings.presentation

import com.jefisu.domain.model.User

data class SettingsState(
    val user: User? = null,
    val isLanguagePickerVisible: Boolean = false,
    val isCurrencyPickerVisible: Boolean = false,
)
