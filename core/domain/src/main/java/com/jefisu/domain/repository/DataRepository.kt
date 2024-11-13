package com.jefisu.domain.repository

import com.jefisu.domain.model.Card
import com.jefisu.domain.model.Category
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import kotlinx.coroutines.flow.Flow

typealias SubscriptionRepository = DataRepository<Subscription>
typealias CategoryRepository = DataRepository<Category>
typealias CardRepository = DataRepository<Card>

interface DataRepository<T> {
    val allData: Flow<List<T>>
    suspend fun getById(id: String): T?
    suspend fun insert(obj: T): Result<Unit, DataMessage>
    suspend fun delete(obj: T): Result<Unit, DataMessage>
}
