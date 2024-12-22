package com.jefisu.data.repository

import com.jefisu.data.local.DataSources
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.mapper.toCard
import com.jefisu.data.mapper.toCardOffline
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Card
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val dataSources: DataSources,
) : DataRepositoryImpl<Card, CardOffline>(
    dataSource = dataSources.card,
    pendingSyncDataSource = dataSources.pendingSync,
    input = Card::toCardOffline,
    output = CardOffline::toCard,
)