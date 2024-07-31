package com.jefisu.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = combine(_state, repository.subscriptions) { state, subscriptions ->
        state.copy(
            subscriptions = subscriptions,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        HomeState(),
    )
}
