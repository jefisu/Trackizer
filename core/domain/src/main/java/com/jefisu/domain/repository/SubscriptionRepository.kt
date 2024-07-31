package com.jefisu.domain.repository

import com.jefisu.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    val subscriptions: Flow<List<Subscription>>
}
