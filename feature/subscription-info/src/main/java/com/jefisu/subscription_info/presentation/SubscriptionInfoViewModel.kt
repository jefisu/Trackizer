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
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SubscriptionInfoViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
    private val navigator: Navigator,
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
            is SubscriptionInfoAction.ToggleSubscriptionInfoSheet -> selectInfo(action.infoRow)

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

            SubscriptionInfoAction.NavigateBack -> {
                viewModelScope.launch { navigator.navigateUp() }
            }
        }
    }

    private fun selectInfo(info: InfoRow?) {
        if (info?.type == InfoRowType.Name) return

        if (
            info?.type == InfoRowType.Category &&
            state.categories.isEmpty() ||
            info?.type == InfoRowType.CreditCard &&
            state.creditCards.isEmpty()
        ) {
            state = state.copy(selectedInfoRow = null)
            DataMessage.NO_DATA_AVAILABLE
                .asMessageText(Argument(info.type.titleId))
                .also(MessageController::sendMessage)
            return
        }

        state = state.copy(
            selectedInfoRow = info,
            description = state.subscription?.description.orEmpty(),
            reminder = state.subscription?.reminder ?: false,
        )
        viewModelScope.launch {
            UiEventController.sendEvent(SubscriptionInfoEvent.ToogleBottomSheet)
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
                        navigator.navigateUp()
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
                        navigator.navigateUp()
                    }
                    .onError { MessageController.sendMessage(it.asMessageText()) }
            }
        }
    }

    fun checkDataChanges() {
        _subscription?.let {
            val hasUnsavedChanges = mapOf(
                it.service to state.subscription?.service,
                it.description to state.subscription?.description,
                it.price to state.subscription?.price,
                it.firstPayment to state.subscription?.firstPayment,
                it.reminder to state.subscription?.reminder,
                it.category?.id to state.subscription?.category?.id,
                it.card?.id to state.subscription?.card?.id,
            ).any { (old, current) ->
                old != current
            }
            state = state.copy(hasUnsavedChanges = hasUnsavedChanges)
        }
    }
}