package com.jefisu.data.repository

import com.jefisu.data.local.DataSources
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.mapper.toSubscription
import com.jefisu.data.mapper.toSubscriptionOffline
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.CategoryRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

@Singleton
class SubscriptionRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val dataSources: DataSources,
    private val categoryRepository: CategoryRepository,
    private val cardRepository: CardRepository,
) : DataRepositoryImpl<Subscription, SubscriptionOffline>(
    dataSource = dataSources.subscription,
    pendingSyncDataSource = dataSources.pendingSync,
    input = Subscription::toSubscriptionOffline,
    output = SubscriptionOffline::toSubscription,
) {

    override val allData = dataSources.subscription
        .allData
        .map { list -> list.map { it.loadCategoryAndCard() } }
        .flowOn(dispatcher.io)

    private suspend fun SubscriptionOffline.loadCategoryAndCard(): Subscription {
        return withContext(dispatcher.io) {
            val category = async { categoryRepository.getById(categoryId ?: return@async null) }
            val card = async { cardRepository.getById(cardId ?: return@async null) }
            toSubscription().copy(
                category = category.await(),
                card = card.await(),
            )
        }
    }
}
