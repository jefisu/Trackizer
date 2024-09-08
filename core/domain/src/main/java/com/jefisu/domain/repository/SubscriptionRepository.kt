package com.jefisu.domain.repository

import com.jefisu.domain.model.Subscription
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.EmptyResult
import com.jefisu.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    val subscriptions: Flow<List<Subscription>>

    suspend fun getSubscriptionById(id: String): Subscription?
    suspend fun addSubscription(subscription: Subscription): EmptyResult
    suspend fun deleteSubscription(id: String): Result<DataMessage, DataMessage>
}
