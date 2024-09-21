package com.jefisu.spending_budgets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.designsystem.components.toDecimalValue
import com.jefisu.domain.model.Category
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.spending_budgets.domain.validation.categoryBudgetValidate
import com.jefisu.spending_budgets.domain.validation.categoryNameValidate
import com.jefisu.ui.MessageController
import com.jefisu.ui.UiEventController
import com.jefisu.ui.asMessageText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun onAction(action: SpendingBudgetsAction) {
        when (action) {
            is SpendingBudgetsAction.CategoryNameChanged -> {
                _state.update { it.copy(categoryName = action.name) }
            }

            is SpendingBudgetsAction.CategoryBudgetChanged -> {
                _state.update { it.copy(categoryBudget = action.budget) }
            }

            is SpendingBudgetsAction.CategorTypeChanged -> {
                _state.update { it.copy(categoryType = action.type) }
            }

            is SpendingBudgetsAction.ToggleAddCategoryBottomSheet -> {
                _state.update {
                    it.copy(
                        showAddCategoryBottomSheet = !it.showAddCategoryBottomSheet,
                        category = null,
                    )
                }
                action.category?.let {
                    _state.update {
                        it.copy(
                            category = action.category,
                            categoryName = action.category.name,
                            categoryBudget = toCurrencyString(action.category.budget),
                            categoryType = action.category.type,
                        )
                    }
                }
            }

            is SpendingBudgetsAction.ToogleCategoryTypePicker -> {
                _state.update {
                    it.copy(isSelectingCategoryType = !it.isSelectingCategoryType)
                }
            }

            is SpendingBudgetsAction.ToogleDeleteAlert -> {
                _state.update {
                    it.copy(
                        showDeleteAlert = !it.showDeleteAlert,
                        category = action.category,
                    )
                }
            }

            is SpendingBudgetsAction.AddCategory -> addCategory()

            is SpendingBudgetsAction.DeleteCategory -> deleteCategory()
        }
    }

    private fun addCategory() {
        viewModelScope.launch {
            val nameResult = categoryNameValidate.validate(_state.value.categoryName)
            val budgetResult = categoryBudgetValidate.validate(
                _state.value.categoryBudget.toDecimalValue(),
            )

            val results = listOf(nameResult, budgetResult)
            if (results.any { !it.successfully }) {
                val error = results.firstNotNullOf { it.error }
                MessageController.sendMessage(error.asMessageText())
                cancel(error.toString())
            }

            val category = Category(
                id = state.value.category?.id.orEmpty(),
                name = _state.value.categoryName,
                budget = _state.value.categoryBudget.toDecimalValue(),
                type = state.value.categoryType,
            )
            categoryRepository.addCategory(category)
                .onSuccess {
                    UiEventController.sendEvent(SpendingBudgetsEvent.HideAddCategoryBottomSheet)
                }
                .onError {
                    MessageController.sendMessage(it as MessageText)
                }
        }
    }

    private fun deleteCategory() {
        _state.value.category?.let {
            viewModelScope.launch {
                categoryRepository.deleteCategory(it)
            }
        }
    }

    private fun toCurrencyString(value: Float): String = value
        .toString()
        .split(".")
        .let { parts ->
            val decimalPart = parts[1].run {
                val zero = '0'
                if (any { it == zero }) return@run ""
                if (length == 1) return@run plus(zero)
                this
            }
            "${parts[0]}$decimalPart"
        }
}
