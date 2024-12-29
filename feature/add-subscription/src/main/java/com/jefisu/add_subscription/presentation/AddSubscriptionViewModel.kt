package com.jefisu.add_subscription.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.add_subscription.domain.validation.subscriptionPriceValidate
import com.jefisu.designsystem.components.toDecimalValue
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.ui.MessageController
import com.jefisu.ui.navigation.Navigator
import com.jefisu.ui.util.asMessageText
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AddSubscriptionViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
    private val navigator: Navigator,
) : ViewModel() {

    private val _state = MutableStateFlow(AddSubscriptionState())
    val state = _state.asStateFlow()

    fun onAction(action: AddSubscriptionAction) {
        when (action) {
            is AddSubscriptionAction.DescriptionChanged -> {
                _state.update { it.copy(description = action.description) }
            }

            is AddSubscriptionAction.PriceChanged -> {
                _state.update { it.copy(price = action.price) }
            }

            is AddSubscriptionAction.SubscriptionServiceChanged -> {
                _state.update { it.copy(selectedService = action.service) }
            }

            AddSubscriptionAction.AddSubscription -> addSubscription()

            AddSubscriptionAction.NavigateUp -> navigateBack()
        }
    }

    private fun addSubscription() {
        viewModelScope.launch {
            val priceResult =
                subscriptionPriceValidate.validate(_state.value.price.toDecimalValue())
            if (!priceResult.successfully) {
                MessageController.sendMessage(priceResult.error!!.asMessageText())
                return@launch
            }

            val subscription = Subscription(
                service = _state.value.selectedService,
                description = _state.value.description,
                price = _state.value.price.toDecimalValue(),
                firstPayment = LocalDate.now(),
                reminder = false,
            )
            subscriptionRepository.insert(subscription)
                .onSuccess {
                    navigator.navigateUp()
                }
                .onError { error ->
                    MessageController.sendMessage(error.asMessageText())
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch { navigator.navigateUp() }
    }
}