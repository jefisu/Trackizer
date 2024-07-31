package com.jefisu.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.jefisu.data.dto.SubscriptionDto
import com.jefisu.data.mapper.toSubscription
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.SubscriptionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SubscriptionRepositoryImpl(private val dispatcher: CoroutineDispatcher) :
    SubscriptionRepository {

    private val userId = Firebase.auth.currentUser?.uid
    private val reference = Firebase.firestore.collection("subscriptions")

    override val subscriptions: Flow<List<Subscription>> = reference
        .whereEqualTo("userId", userId)
        .snapshots()
        .map { query ->
            query
                .toObjects<SubscriptionDto>()
                .map { it.toSubscription() }
        }
        .flowOn(dispatcher)
}
