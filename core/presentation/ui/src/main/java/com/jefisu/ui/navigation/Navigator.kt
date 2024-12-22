package com.jefisu.ui.navigation

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

interface Navigator {

    val startDestination: Destination
    val actions: Flow<NavigatorAction>
    val currentDestination: StateFlow<Destination>

    suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit = {},
    )

    suspend fun navigateUp()
    fun updateCurrentDestination(destination: Destination)
}

class DefaultNavigator(override val startDestination: Destination) : Navigator {

    private val _actions = Channel<NavigatorAction>()
    override val actions = _actions.receiveAsFlow()

    private val _currentDestination = MutableStateFlow(startDestination)
    override val currentDestination = _currentDestination.asStateFlow()

    override suspend fun navigate(
        destination: Destination,
        navOptions: NavOptionsBuilder.() -> Unit,
    ) {
        _actions.send(NavigatorAction.Navigate(destination, navOptions))
    }

    override suspend fun navigateUp() {
        _actions.send(NavigatorAction.NavigateUp)
    }

    override fun updateCurrentDestination(destination: Destination) {
        _currentDestination.update { destination }
    }
}