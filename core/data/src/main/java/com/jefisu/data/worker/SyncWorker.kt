package com.jefisu.data.worker

import android.content.Context
import androidx.compose.ui.util.fastFilter
import androidx.compose.ui.util.fastForEach
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jefisu.data.local.DataSources
import com.jefisu.data.local.OfflineDataSource
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.OfflineObject
import com.jefisu.data.local.model.PendingSyncItem
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.local.model.SyncType
import com.jefisu.data.mapper.toCardDocument
import com.jefisu.data.mapper.toCategoryDocument
import com.jefisu.data.mapper.toSubscriptionDocument
import com.jefisu.data.remote.FirestoreDocumentSync
import com.jefisu.data.remote.FirestoreSyncWrapper
import com.jefisu.data.remote.RemoteDataSource
import com.jefisu.data.util.safeCall
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.repository.SettingsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dataSources: DataSources,
    private val firestoreWrapper: FirestoreSyncWrapper,
    private val settingsRepository: SettingsRepository,
    private val dispatcher: DispatcherProvider,
) : CoroutineWorker(context, params) {

    private val mutex = Mutex()

    override suspend fun doWork(): Result {
        withContext(dispatcher.io) {
            val pendingSyncItems = getPendingSyncItems()
            pendingSyncItems.fastForEach { item ->
                mutex.withLock(item.offlineId) {
                    safeCall(
                        dispatcher = dispatcher.io,
                        causedException = "Sync to FirestoreCloud - id: ${item.offlineId} - type: ${item.type}",
                    ) {
                        syncData(item)
                    }
                }
            }
            pendingSyncItems.fastForEach { item ->
                mutex.withLock(item.offlineId) {
                    dataSources.pendingSync.deleteById(item._id.toHexString())
                }
            }
        }
        return Result.success()
    }

    private suspend fun getPendingSyncItems(): List<PendingSyncItem> = dataSources.pendingSync
        .allData
        .combine(settingsRepository.settings) { data, settings ->
            data.fastFilter { !it.isSynced && settings.isCloudSyncEnabled }
        }
        .first()

    private suspend fun syncData(pendingSyncItem: PendingSyncItem) {
        when (SyncType.valueOf(pendingSyncItem.type)) {
            SyncType.SUBSCRIPTION -> {
                sync(
                    pendingSyncItem,
                    dataSources.subscription,
                    firestoreWrapper.subscription,
                    SubscriptionOffline::toSubscriptionDocument,
                )
            }

            SyncType.CATEGORY -> {
                sync(
                    pendingSyncItem,
                    dataSources.category,
                    firestoreWrapper.category,
                    CategoryOffline::toCategoryDocument,
                )
            }

            SyncType.CARD -> {
                sync(
                    pendingSyncItem,
                    dataSources.card,
                    firestoreWrapper.card,
                    CardOffline::toCardDocument,
                )
            }

            else -> Unit
        }
    }

    private suspend fun <T : OfflineObject, R : FirestoreDocumentSync> sync(
        pendingSyncItem: PendingSyncItem,
        dataSource: OfflineDataSource<T>,
        firestoreDataSource: RemoteDataSource<R>,
        transform: (T) -> R,
    ) {
        val data = dataSource.getById(pendingSyncItem.offlineId!!)
        data?.let {
            firestoreDataSource.insert(transform(it))
        }
    }

    companion object {
        const val TAG = "SyncWorker"
    }
}
