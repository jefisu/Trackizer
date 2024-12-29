package com.jefisu.subscription_info.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.Argument
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.subscription_info.presentation.util.InfoRow
import com.jefisu.subscription_info.presentation.util.InfoRowType
import com.jefisu.ui.MessageController
import com.jefisu.ui.UiEventController
import com.jefisu.ui.navigation.Navigator
import com.jefisu.ui.util.asMessageText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionInfoViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
    private val navigator: Navigator,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(SubscriptionInfoState())
    val state = combine(
        _state,
        categoryRepository.allData,
        cardRepository.allData,
    ) { _, categories, cards ->
        _state.updateAndGet {
            it.copy(
                categories = categories,
                creditCards = cards,
            )
        }
    }.onStart { getSubscription() }
        .onEach { checkDataChanges() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            SubscriptionInfoState(),
        )

    private var _subscription: Subscription? = null

    fun onAction(action: SubscriptionInfoAction) {
        when (action) {
            is SubscriptionInfoAction.DeleteSubscription -> deleteSubscription()
            is SubscriptionInfoAction.SaveSubscription -> saveSubscription()
            is SubscriptionInfoAction.ToggleSubscriptionInfoSheet -> selectInfo(action.infoRow)

            is SubscriptionInfoAction.DescriptionChanged -> {
                if (action.applyChanges) {
                    _state.update {
                        it.copy(
                            subscription = it.subscription?.copy(description = action.description),
                        )
                    }
                    return
                }
                _state.update { it.copy(description = action.description) }
            }

            is SubscriptionInfoAction.CategoryChanged -> {
                _state.update {
                    it.copy(
                        subscription = it.subscription?.copy(
                            category = action.category,
                        ),
                    )
                }
            }

            is SubscriptionInfoAction.CreditCardChanged -> {
                _state.update {
                    it.copy(
                        subscription = it.subscription?.copy(
                            card = action.creditCard,
                        ),
                    )
                }
            }

            is SubscriptionInfoAction.FirstPaymentChanged -> {
                _state.update {
                    it.copy(
                        subscription = it.subscription?.copy(
                            firstPayment = action.date,
                        ),
                    )
                }
            }

            is SubscriptionInfoAction.ReminderChanged -> {
                if (action.applyChanges) {
                    _state.update {
                        it.copy(
                            subscription = it.subscription?.copy(
                                reminder = action.reminder,
                            ),
                        )
                    }
                    return
                }
                _state.update { it.copy(reminder = action.reminder) }
            }

            SubscriptionInfoAction.NavigateBack -> {
                viewModelScope.launch { navigator.navigateUp() }
            }
        }
    }

    private fun selectInfo(info: InfoRow?) {
        if (info?.type == InfoRowType.Name) return

        if (
            info?.type == InfoRowType.Category &&
            _state.value.categories.isEmpty() ||
            info?.type == InfoRowType.CreditCard &&
            _state.value.creditCards.isEmpty()
        ) {
            _state.update { it.copy(selectedInfoRow = null) }
            DataMessage.NO_DATA_AVAILABLE
                .asMessageText(Argument(info.type.titleId))
                .also(MessageController::sendMessage)
            return
        }

        _state.update {
            it.copy(
                selectedInfoRow = info,
                description = _state.value.subscription?.description.orEmpty(),
                reminder = _state.value.subscription?.reminder ?: false,
            )
        }
        viewModelScope.launch {
            UiEventController.sendEvent(SubscriptionInfoEvent.ToogleBottomSheet)
        }
    }

    private fun getSubscription() {
        viewModelScope.launch {
            savedStateHandle.get<String>("id")?.let { id ->
                subscriptionRepository.getById(id)?.let { subscription ->
                    _state.update { it.copy(subscription = subscription) }
                    _subscription = subscription
                }
            }
        }
    }

    private fun saveSubscription() {
        if (_state.value.subscription == null) return
        viewModelScope.launch {
            if (!_state.value.hasUnsavedChanges) {
                navigator.navigateUp()
                return@launch
            }
            subscriptionRepository.insert(_state.value.subscription!!)
                .onSuccess { navigator.navigateUp() }
                .onError { MessageController.sendMessage(it.asMessageText()) }
        }
    }

    private fun deleteSubscription() {
        if (_state.value.subscription == null) return
        viewModelScope.launch {
            subscriptionRepository.delete(_state.value.subscription!!)
                .onSuccess { navigator.navigateUp() }
                .onError { MessageController.sendMessage(it.asMessageText()) }
        }
    }

    private fun checkDataChanges() {
        _subscription?.let {
            val subscription = _state.value.subscription
            val hasUnsavedChanges = mapOf(
                it.service to subscription?.service,
                it.description to subscription?.description,
                it.price to subscription?.price,
                it.firstPayment to subscription?.firstPayment,
                it.reminder to subscription?.reminder,
                it.category?.id to subscription?.category?.id,
                it.card?.id to subscription?.card?.id,
            ).any { (old, current) ->
                old != current
            }
            _state.update { it.copy(hasUnsavedChanges = hasUnsavedChanges) }
        }
    }
}