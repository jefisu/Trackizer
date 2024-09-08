package com.jefisu.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.jefisu.data.dto.SubscriptionDto
import com.jefisu.data.mapper.toSubscription
import com.jefisu.data.mapper.toSubscriptionDto
import com.jefisu.data.util.fromCurrentUser
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.EmptyResult
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.UiText
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SubscriptionRepositoryImpl(
    private val dispatcher: DispatcherProvider,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
) : SubscriptionRepository {

    private val userId = Firebase.auth.currentUser?.uid
    private val collection = Firebase.firestore.collection("subscriptions")

    override val subscriptions: Flow<List<Subscription>> = collection
        .fromCurrentUser()
        .snapshots()
        .map { query ->
            query
                .toObjects<SubscriptionDto>()
                .map { it.toSubscription() }
        }
        .flowOn(dispatcher.io)

    override suspend fun getSubscriptionById(id: String): Subscription? =
        withContext(dispatcher.io) {
            collection
                .document(id)
                .get()
                .await()
                .toObject<SubscriptionDto>()
                .let {
                    val categoryJob = async {
                        categoryRepository.getCategoryById(it?.id ?: return@async null)
                    }
                    val cardJob = async {
                        cardRepository.getCardById(it?.id ?: return@async null)
                    }
                    it?.toSubscription()?.copy(
                        category = categoryJob.await(),
                        card = cardJob.await(),
                    )
                }
        }

    override suspend fun addSubscription(subscription: Subscription): EmptyResult =
        withContext(dispatcher.io) {
            try {
                val subscriptionDto = subscription
                    .toSubscriptionDto()
                    .copy(userId = userId)

                subscriptionDto.id?.let { id ->
                    collection
                        .document(id)
                        .set(subscriptionDto)
                        .await()
                    return@withContext Result.Success(Unit)
                }

                collection
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

    override suspend fun deleteSubscription(id: String): Result<DataMessage, DataMessage> =
        withContext(dispatcher.io) {
            try {
                collection.document(id).delete().await()
                Result.Success(DataMessage.SUBSCRIPTION_DELETED)
            } catch (e: FirebaseFirestoreException) {
                Result.Error(DataMessage.SUBSCRIPTION_NOT_DELETED)
            }
        }
}
