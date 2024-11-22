package com.jefisu.subscription_info.presentation

import com.jefisu.domain.model.Card
import com.jefisu.domain.model.Category
import com.jefisu.subscription_info.presentation.util.InfoRow
import java.time.LocalDate

sealed interface SubscriptionInfoAction {
    data object DeleteSubscription : SubscriptionInfoAction
    data object SaveSubscription : SubscriptionInfoAction
    data class ToggleSubscriptionInfoSheet(val infoRow: InfoRow? = null) : SubscriptionInfoAction
    data class DescriptionChanged(val description: String, val applyChanges: Boolean = false) :
        SubscriptionInfoAction

    data class CategoryChanged(val category: Category) : SubscriptionInfoAction

    data class CreditCardChanged(val creditCard: Card) : SubscriptionInfoAction

    data class FirstPaymentChanged(val date: LocalDate) : SubscriptionInfoAction

    data class ReminderChanged(val reminder: Boolean, val applyChanges: Boolean = false) :
        SubscriptionInfoAction

    data object NavigateBack : SubscriptionInfoAction
}
