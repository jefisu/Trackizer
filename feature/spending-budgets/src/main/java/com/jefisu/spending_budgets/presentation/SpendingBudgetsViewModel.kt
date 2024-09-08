package com.jefisu.spending_budgets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.SubscriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SpendingBudgetsViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SpendingBudgetsState())
    val state = combine(
        _state,
        categoryRepository.categories,
        subscriptionRepository.subscriptions,
    ) { state, categories, subscriptions ->
        val categoriesWithUsedBudget = categories.map { category ->
            val usedBudget = subscriptions
                .filter { it.category?.id == category.id }
                .sumOf { it.price.toDouble() }
                .toFloat()
            category.copy(usedBudget = usedBudget)
        }
        state.copy(categories = categoriesWithUsedBudget)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        SpendingBudgetsState(),
    )
}
