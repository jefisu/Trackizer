package com.jefisu.designsystem.util

import androidx.compose.runtime.compositionLocalOf
import com.jefisu.domain.model.Settings
import com.jefisu.domain.model.User

data class AppConfig(val settings: Settings = Settings(), val user: User? = null)

val LocalAppConfig = compositionLocalOf { AppConfig() }
