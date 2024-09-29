package com.jefisu.data.repository

import com.jefisu.data.local.OfflineDataSource
import com.jefisu.data.local.PendingSyncDataSource
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.OfflineObject
import com.jefisu.data.local.model.PendingSyncItem
import com.jefisu.data.local.model.SyncAction
import com.jefisu.data.local.model.SyncType
import com.jefisu.domain.repository.DataRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataRepositoryImpl<T : OfflineObject, R>(
    private val dataSource: OfflineDataSource<T>,
    private val pendingSyncDataSource: PendingSyncDataSource,
    private val input: (R) -> T,
    private val output: (T) -> R,
) : DataRepository<R> {

    override val allData: Flow<List<R>> = dataSource.allData.map { it.map(output) }

    override suspend fun getById(id: String): R? = dataSource.getById(id)?.let(output)

    override suspend fun insert(obj: R): Result<Unit, DataMessage> {
        dataSource.insertOrUpdate(input(obj)).onSuccess { offlineId ->
            schedulePendingSync(
                offlineId,
                SyncAction.INSERT_OR_UPDATE,
            )
        }
        return Result.Success(Unit)
    }

    override suspend fun deleteById(id: String) = dataSource.deleteById(id).onSuccess {
        schedulePendingSync(id, SyncAction.DELETE)
    }

    private suspend fun schedulePendingSync(
        id: String,
        action: SyncAction,
    ) {
        val obj = dataSource.getById(id) ?: return
        val item = PendingSyncItem().apply {
            cloudId = obj.cloudId
            offlineId = id
            this.action = action.name
            type = when (obj) {
                is CategoryOffline -> SyncType.CATEGORY
                is CardOffline -> SyncType.CARD
                else -> SyncType.NONE
            }.name
        }
        pendingSyncDataSource.insertOrUpdate(item)
    }
}
