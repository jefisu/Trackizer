package com.jefisu.add_subscription

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.ui.MessageController
import com.jefisu.ui.UiEventController
import com.jefisu.ui.event.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddSubscriptionViewModel @Inject constructor(
    private val subscriptionRepository: SubscriptionRepository,
) : ViewModel() {

    var state by mutableStateOf(AddSubscriptionState())
        private set

    fun onAction(action: AddSubscriptionAction) {
        when (action) {
            is AddSubscriptionAction.DescriptionChanged -> {
                state = state.copy(description = action.description)
            }

            is AddSubscriptionAction.PriceChanged -> {
                state = state.copy(price = action.price)
            }

            is AddSubscriptionAction.SubscriptionServiceChanged -> {
                state = state.copy(selectedService = action.service)
            }

            AddSubscriptionAction.AddSubscription -> addSubscription()
        }
    }

    private fun addSubscription() {
        viewModelScope.launch {
            val price = state.price.toFloat() / 100f
            val subscription = Subscription(
                id = "",
                cardId = "",
                categoryId = "",
                service = state.selectedService,
                description = state.description,
                price = price,
                paymentDate = LocalDate.now(),
                reminder = false,
            )
            subscriptionRepository.addSubscription(subscription)
                .onSuccess {
                    UiEventController.sendEvent(NavigationEvent.NavigateUp)
                }
                .onError { error ->
                    MessageController.sendMessage(error as MessageText)
                }
        }
    }
}
