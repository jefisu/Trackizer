package com.jefisu.trackizer

import com.jefisu.domain.model.Settings
import com.jefisu.domain.util.MessageText

data class AppState(val settings: Settings = Settings(), val message: MessageText? = null)