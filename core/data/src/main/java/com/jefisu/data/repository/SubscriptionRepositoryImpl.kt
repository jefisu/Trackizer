package com.jefisu.data.repository

import com.jefisu.data.local.DataSources
import com.jefisu.data.local.model.PendingSyncItem
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.local.model.SyncAction
import com.jefisu.data.local.model.SyncType
import com.jefisu.data.mapper.toSubscription
import com.jefisu.data.mapper.toSubscriptionOffline
import com.jefisu.data.util.safeCall
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.map

class SubscriptionRepositoryImpl(
    private val dispatcher: DispatcherProvider,
    private val dataSources: DataSources,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
) : SubscriptionRepository {

    override val allData = dataSources.subscription.allData.map {
        it.map(SubscriptionOffline::toSubscription)
    }

    override suspend fun getById(id: String): Subscription? {
        return safeCall(
            dispatcher = dispatcher.io,
            causedException = "getById - SubscriptionRepository",
        ) {
            val subscriptionOffline = dataSources.subscription.getById(id) ?: return@safeCall null
            val categoryJob = async {
                categoryRepository.getById(subscriptionOffline.categoryId.orEmpty())
            }
            val cardJob = async {
                cardRepository.getById(subscriptionOffline.cardId.orEmpty())
            }
            subscriptionOffline.toSubscription().copy(
                category = categoryJob.await(),
                card = cardJob.await(),
            )
        }
    }

    override suspend fun insert(obj: Subscription): Result<Unit, DataMessage> {
        dataSources.subscription
            .insertOrUpdate(obj.toSubscriptionOffline())
            .onSuccess { offlineId ->
                schedulePendingSync(offlineId, SyncAction.INSERT_OR_UPDATE)
            }
        return Result.Success(Unit)
    }

    override suspend fun deleteById(id: String): Result<Unit, DataMessage> =
        dataSources.subscription.deleteById(id)

    private suspend fun schedulePendingSync(
        id: String,
        action: SyncAction,
    ) {
        val obj = dataSources.subscription.getById(id) ?: return
        val item = PendingSyncItem().apply {
            cloudId = obj.cloudId
            offlineId = id
            this.action = action.name
            type = SyncType.SUBSCRIPTION.name
        }
        dataSources.pendingSync.insertOrUpdate(item)
    }
}
