package com.jefisu.data.local

import com.jefisu.data.StandardDispatcherProvider
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.OfflineObject
import com.jefisu.data.local.model.PendingSyncItem
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
typealias PendingSyncDataSource = OfflineDataSource<PendingSyncItem>

class OfflineDataSource<T : OfflineObject>(
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
        causedException = "getById - $clazz",
    ) {
        realm.query(clazz, "_id == $0", ObjectId(id)).first().find()
    }

    suspend fun insertOrUpdate(obj: T): Result<String, DataMessage> = safeCallResult(
        dispatcher = dispatcher.io,
        causedException = "insertOrUpdate - $clazz",
    ) {
        val objWithId = realm.write { copyToRealm(obj, UpdatePolicy.ALL) }
        objWithId._id.toHexString()
    }

    suspend fun deleteById(id: String): Result<Unit, DataMessage> = safeCallResult(
        dispatcher = dispatcher.io,
    ) {
        realm.write {
            val objToDelete = query(clazz, "_id == $0", ObjectId(id)).find().first()
            findLatest(objToDelete)?.let(::delete)
        }
    }
}
