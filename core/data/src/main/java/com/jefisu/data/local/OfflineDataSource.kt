package com.jefisu.data.local

import com.jefisu.data.StandardDispatcherProvider
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.Offline
import com.jefisu.data.local.model.PendingSync
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.util.safeCall
import com.jefisu.data.util.safeCallResult
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

typealias SubscriptionDataSource = OfflineDataSource<SubscriptionOffline>
typealias CardDataSource = OfflineDataSource<CardOffline>
typealias CategoryDataSource = OfflineDataSource<CategoryOffline>
typealias PendingSyncDataSource = OfflineDataSource<PendingSync>

class OfflineDataSource<T : Offline>(
    private val clazz: KClass<T>,
    private val realm: Realm,
    private val dispatcher: DispatcherProvider = StandardDispatcherProvider,
) {

    val allData: Flow<List<T>> = realm
        .query(clazz)
        .asFlow()
        .map { it.list }

    suspend fun getById(id: String): T? = safeCall(
        dispatcher = dispatcher.io,
        causedException = "getById - ${clazz.simpleName}",
    ) {
        realm.query(clazz, "_id == $0", ObjectId(id)).find().firstOrNull()
    }

    suspend fun insertOrUpdate(obj: T): Result<Unit, DataMessage> = safeCallResult(
        dispatcher = dispatcher.io,
        causedException = "insertOrUpdate - ${clazz.simpleName}",
    ) {
        realm.write { copyToRealm(obj, UpdatePolicy.ALL) }
    }

    suspend fun delete(obj: T): Result<Unit, DataMessage> = safeCallResult(
        dispatcher = dispatcher.io,
        causedException = "delete - ${clazz.simpleName}: ${obj._id.toHexString()}",
    ) {
        realm.write {
            val dataObj = this.query(clazz, "_id == $0", obj._id).find().first()
            findLatest(dataObj)?.let(::delete)
        }
    }
}