package com.jefisu.subscription_info.presentation

import com.jefisu.domain.model.Card
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.Subscription
import com.jefisu.subscription_info.presentation.util.InfoRow

data class SubscriptionInfoState(
    val subscription: Subscription? = null,
    val showSubscriptionInfoSheet: Boolean = false,
    val selectedInfoRow: InfoRow? = null,
    val categories: List<Category> = emptyList(),
    val creditCards: List<Card> = emptyList(),
    val description: String = "",
    val reminder: Boolean = false,
    val showDeleteAlert: Boolean = false,
    val showUnsavedChangesAlert: Boolean = false,
    val hasUnsavedChanges: Boolean = false,
)
