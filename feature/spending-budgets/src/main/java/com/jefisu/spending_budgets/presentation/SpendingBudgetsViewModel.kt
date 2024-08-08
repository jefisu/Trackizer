package com.jefisu.spending_budgets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SpendingBudgetsViewModel(private val repository: SubscriptionRepository) : ViewModel() {

    private val _state = MutableStateFlow(SpendingBudgetsState())
    val state = combine(
        _state,
        repository.categories,
        repository.subscriptions,
    ) { state, categories, subscriptions ->
        val categoriesWithUsedBudget = categories.map { category ->
            val usedBudget = subscriptions
                .filter { it.categoryId == category.id }
                .sumOf { it.price.toDouble() }
                .toFloat()
            category.copy(usedBudget = usedBudget)
        }
        state.copy(categories = categoriesWithUsedBudget)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SpendingBudgetsState(),
    )
}
