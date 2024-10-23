package com.jefisu.ui.navigation

import androidx.navigation.NavOptionsBuilder

sealed interface NavigatorAction {

    data class Navigate(
        val destination: Destination,
        val navOptions: NavOptionsBuilder.() -> Unit,
    ) : NavigatorAction

    data object NavigateUp : NavigatorAction
}
