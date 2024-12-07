package com.jefisu.settings.presentation

import com.jefisu.domain.model.Settings
import com.jefisu.domain.model.User

data class SettingsState(
    val user: User? = null,
    val settings: Settings = Settings(),
    val canDeleteAccount: Boolean = false,
    val userPassword: String = "",
    val deleteAccountInProgress: Boolean = false,
)
