package com.jefisu.home.presentation

import com.jefisu.ui.navigation.Destination

sealed interface HomeAction {
    data class Navigate(val destination: Destination) : HomeAction
}