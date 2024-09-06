package com.jefisu.domain.repository

import com.jefisu.domain.model.Category
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    val subscriptions: Flow<List<Subscription>>
    val categories: Flow<List<Category>>

    suspend fun addSubscription(subscription: Subscription): EmptyResult
}
