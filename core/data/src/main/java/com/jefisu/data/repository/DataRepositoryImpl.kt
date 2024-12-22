package com.jefisu.data.repository

import com.jefisu.data.local.OfflineDataSource
import com.jefisu.data.local.PendingSyncDataSource
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.Offline
import com.jefisu.data.local.model.PendingSync
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.local.model.SyncAction
import com.jefisu.domain.model.BaseDomain
import com.jefisu.domain.repository.DataRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

open class DataRepositoryImpl<IN : BaseDomain, OUT : Offline>(
    private val dataSource: OfflineDataSource<OUT>,
    private val pendingSyncDataSource: PendingSyncDataSource,
    private val input: (IN) -> OUT,
    private val output: (OUT) -> IN,
) : DataRepository<IN> {

    override val allData: Flow<List<IN>> = dataSource.allData.map { it.map(output) }

    override suspend fun getById(id: String): IN? = allData.first().find { it.id == id }

    override suspend fun insert(obj: IN): Result<Unit, DataMessage> {
        val offline = input(obj)
        return dataSource.insertOrUpdate(offline).onSuccess {
            schedulePendingSync(
                offline,
                SyncAction.Type.INSERT_OR_UPDATE,
            )
        }
    }

    override suspend fun delete(obj: IN): Result<Unit, DataMessage> {
        val offline = input(obj)
        return dataSource.delete(offline).onSuccess {
            schedulePendingSync(
                offline,
                SyncAction.Type.DELETE,
            )
        }
    }

    private suspend fun schedulePendingSync(
        obj: OUT,
        syncActionType: SyncAction.Type,
    ) {
        val pendingSync = pendingSyncDataSource.allData.first().find {
            it.dataId == obj._id.toHexString()
        }
        val item = PendingSync().apply {
            pendingSync?.let { _id = it._id }
            dataId = obj._id.toHexString()
            action = SyncAction.set(
                type = syncActionType,
                dataType = when (obj) {
                    is CardOffline -> SyncAction.DataType.CARD
                    is CategoryOffline -> SyncAction.DataType.CATEGORY
                    is SubscriptionOffline -> SyncAction.DataType.SUBSCRIPTION
                    else -> throw IllegalArgumentException("Invalid data type")
                },
            )
        }
        pendingSyncDataSource.insertOrUpdate(item)
    }
}