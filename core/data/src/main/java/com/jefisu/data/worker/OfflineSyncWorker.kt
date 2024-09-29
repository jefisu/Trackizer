package com.jefisu.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jefisu.data.local.DataSources
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.Offline
import com.jefisu.data.local.model.PendingSync
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.local.model.SyncAction
import com.jefisu.data.mapper.toCardDocument
import com.jefisu.data.mapper.toCategoryDocument
import com.jefisu.data.mapper.toSubscriptionDocument
import com.jefisu.data.remote.FirestoreDataSources
import com.jefisu.domain.util.onSuccess
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OfflineSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val firestoreWrapper: FirestoreDataSources,
    private val offlineDataSource: DataSources,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val (pendingSync, offline) = getData() ?: return Result.failure()

        val result = when (pendingSync.action?.type) {
            SyncAction.Type.INSERT_OR_UPDATE -> offline?.addToCloud()
            SyncAction.Type.DELETE -> pendingSync.removeFromCloud()
            else -> null
        }
        result?.onSuccess {
            offlineDataSource.pendingSync.delete(pendingSync)
        }

        return Result.success()
    }

    private suspend fun getData(): Pair<PendingSync, Offline?>? {
        val pendingSyncId = inputData.getString(PENDING_SYNC_ID_KEY) ?: return null
        val pendingSync = offlineDataSource.pendingSync.getById(pendingSyncId) ?: return null

        val offline = pendingSync.dataId?.let {
            with(offlineDataSource) {
                when (pendingSync.action?.dataType) {
                    SyncAction.DataType.SUBSCRIPTION -> subscription.getById(it)
                    SyncAction.DataType.CATEGORY -> category.getById(it)
                    SyncAction.DataType.CARD -> card.getById(it)
                    else -> return null
                }
            }
        }

        return pendingSync to offline
    }

    private suspend fun Offline.addToCloud() = with(firestoreWrapper) {
        when (this@addToCloud) {
            is SubscriptionOffline -> subscription.insertOrUpdate(toSubscriptionDocument())
            is CategoryOffline -> category.insertOrUpdate(toCategoryDocument())
            is CardOffline -> card.insertOrUpdate(toCardDocument())
            else -> null
        }
    }

    private suspend fun PendingSync.removeFromCloud() = with(firestoreWrapper) {
        dataId?.let {
            when (action?.dataType) {
                SyncAction.DataType.SUBSCRIPTION -> subscription.deleteById(it)
                SyncAction.DataType.CATEGORY -> category.deleteById(it)
                SyncAction.DataType.CARD -> card.deleteById(it)
                else -> null
            }
        }
    }

    companion object {
        const val PENDING_SYNC_ID_KEY = "PENDING_SYNC_ID_KEY"
    }
}
