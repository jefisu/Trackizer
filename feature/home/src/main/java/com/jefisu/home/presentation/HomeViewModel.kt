package com.jefisu.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.ui.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val categoryRepository: CategoryRepository,
    private val navigator: Navigator,
) : ViewModel() {

    val state = combine(
        categoryRepository.allData,
        subscriptionRepository.allData,
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

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Navigate -> {
                viewModelScope.launch { navigator.navigate(action.destination) }
            }
        }
    }
}
