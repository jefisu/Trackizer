package com.jefisu.subscription_info.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.subscription_info.presentation.util.InfoRowType
import com.jefisu.ui.MessageController
import com.jefisu.ui.UiEventController
import com.jefisu.ui.asMessageText
import com.jefisu.ui.event.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SubscriptionInfoViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var state by mutableStateOf(SubscriptionInfoState())
        private set

    private var _subscription: Subscription? = null

    init {
        getSubscription()
        viewModelScope.launch {
            launch {
                categoryRepository.allData.collect { state = state.copy(categories = it) }
            }
            launch {
                cardRepository.allData.collect { state = state.copy(creditCards = it) }
            }
        }
    }

    fun onAction(action: SubscriptionInfoAction) {
        when (action) {
            is SubscriptionInfoAction.DeleteSubscription -> deleteSubscription()
            is SubscriptionInfoAction.SaveSubscription -> saveSubscription()
            is SubscriptionInfoAction.ToggleSubscriptionInfoSheet -> {
                if (action.infoRow?.type == InfoRowType.Name) {
                    return
                }
                val subscription = state.subscription!!
                state = state.copy(
                    showSubscriptionInfoSheet = !state.showSubscriptionInfoSheet,
                    selectedInfoRow = action.infoRow,
                    description = subscription.description,
                    reminder = subscription.reminder,
                )
            }

            is SubscriptionInfoAction.DescriptionChanged -> {
                if (action.applyChanges) {
                    state = state.copy(
                        subscription = state.subscription?.copy(description = action.description),
                    )
                    return
                }
                state = state.copy(description = action.description)
            }

            is SubscriptionInfoAction.CategoryChanged -> {
                state = state.copy(
                    subscription = state.subscription?.copy(
                        category = action.category,
                    ),
                )
            }

            is SubscriptionInfoAction.CreditCardChanged -> {
                state = state.copy(
                    subscription = state.subscription?.copy(
                        card = action.creditCard,
                    ),
                )
            }

            is SubscriptionInfoAction.FirstPaymentChanged -> {
                state = state.copy(
                    subscription = state.subscription?.copy(
                        firstPayment = action.date,
                    ),
                )
            }

            is SubscriptionInfoAction.ReminderChanged -> {
                if (action.applyChanges) {
                    state = state.copy(
                        subscription = state.subscription?.copy(
                            reminder = action.reminder,
                        ),
                    )
                    return
                }
                state = state.copy(reminder = action.reminder)
            }

            is SubscriptionInfoAction.ToggleDeleteSubscriptionAlert -> {
                state = state.copy(showDeleteSubscriptionAlert = !state.showDeleteSubscriptionAlert)
            }
        }
    }

    private fun getSubscription() {
        savedStateHandle.get<String>("id")?.let { id ->
            viewModelScope.launch {
                subscriptionRepository.getById(id)?.let { subscription ->
                    _subscription = subscription
                    state = state.copy(subscription = subscription)
                }
            }
        }
    }

    private fun saveSubscription() {
        viewModelScope.launch {
            state.subscription?.let { subscription ->
                subscriptionRepository.insert(subscription)
                    .onSuccess {
                        UiEventController.sendEvent(NavigationEvent.NavigateUp)
                    }
                    .onError { MessageController.sendMessage(it.asMessageText()) }
            }
        }
    }

    private fun deleteSubscription() {
        viewModelScope.launch {
            _subscription?.let { subscription ->
                subscriptionRepository.delete(subscription)
                    .onSuccess {
                        UiEventController.sendEvent(NavigationEvent.NavigateUp)
                    }
                    .onError { MessageController.sendMessage(it.asMessageText()) }
            }
        }
    }
}
