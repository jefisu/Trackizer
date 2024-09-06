package com.jefisu.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.jefisu.data.dto.CategoryDto
import com.jefisu.data.dto.SubscriptionDto
import com.jefisu.data.mapper.toCategory
import com.jefisu.data.mapper.toSubscription
import com.jefisu.data.mapper.toSubscriptionDto
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.EmptyResult
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SubscriptionRepositoryImpl : SubscriptionRepository {

    private val dispatcher = Dispatchers.IO
    private val userId = Firebase.auth.currentUser?.uid
    private val userIdKey = "userId"

    private val subsCollection = Firebase.firestore.collection("subscriptions")
    private val categoriesCollection = Firebase.firestore.collection("categories")

    override val subscriptions: Flow<List<Subscription>> = subsCollection
        .whereEqualTo(userIdKey, userId)
        .snapshots()
        .map { query ->
            query
                .toObjects<SubscriptionDto>()
                .map { it.toSubscription() }
        }
        .flowOn(dispatcher)

    override val categories: Flow<List<Category>> = categoriesCollection
        .whereEqualTo(userIdKey, userId)
        .snapshots()
        .map { query ->
            query
                .toObjects<CategoryDto>()
                .map { it.toCategory() }
        }
        .flowOn(dispatcher)

    override suspend fun addSubscription(subscription: Subscription): EmptyResult =
        withContext(dispatcher) {
            try {
                val subscriptionDto = subscription
                    .toSubscriptionDto()
                    .copy(userId = userId)

                subscriptionDto.id?.let { id ->
                    subsCollection
                        .document(id)
                        .set(subscriptionDto)
                        .await()
                    return@withContext Result.Success(Unit)
                }

                subsCollection
                    .add(subscriptionDto)
                    .await()
                Result.Success(Unit)
            } catch (e: FirebaseFirestoreException) {
                Result.Error(
                    MessageText.Error(
                        UiText.DynamicString(e.message.orEmpty()),
                    ),
                )
            }
        }
}
