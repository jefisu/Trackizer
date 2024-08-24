package com.jefisu.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(private val repository: SubscriptionRepository) :
    ViewModel() {

    val state = combine(
        repository.categories,
        repository.subscriptions,
    ) { categories, subscriptions ->
        HomeState(
            subscriptions = subscriptions,
            monthlyBudget = categories.sumOf { it.budget.toDouble() }.toFloat(),
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        HomeState(),
    )
}
