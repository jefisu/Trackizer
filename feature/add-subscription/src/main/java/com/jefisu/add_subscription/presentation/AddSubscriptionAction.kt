package com.jefisu.add_subscription.presentation

import com.jefisu.domain.model.SubscriptionService

sealed interface AddSubscriptionAction {
    data class PriceChanged(val price: String) : AddSubscriptionAction
    data class DescriptionChanged(val description: String) : AddSubscriptionAction
    data class SubscriptionServiceChanged(val service: SubscriptionService) : AddSubscriptionAction
    data object AddSubscription : AddSubscriptionAction

    data object NavigateUp : AddSubscriptionAction
}
