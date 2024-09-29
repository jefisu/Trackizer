package com.jefisu.data.worker

import android.content.Context
import androidx.compose.ui.util.fastForEach
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jefisu.data.local.DataSources
import com.jefisu.data.local.model.OfflineObject
import com.jefisu.data.mapper.toCardOffline
import com.jefisu.data.mapper.toCategoryOffline
import com.jefisu.data.mapper.toSubscriptionOffline
import com.jefisu.data.remote.FirestoreDocumentSync
import com.jefisu.data.remote.FirestoreSyncWrapper
import com.jefisu.data.remote.document.CardDocument
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.data.remote.document.SubscriptionDocument
import com.jefisu.domain.DispatcherProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

@HiltWorker
class InitialDataSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dataSources: DataSources,
    private val firestoreWrapper: FirestoreSyncWrapper,
    private val dispatcher: DispatcherProvider,
) : CoroutineWorker(context, params) {

    private val _mutex = Mutex()

    override suspend fun doWork(): Result {
        withContext(dispatcher.io) {
            launch {
                syncFromCloud(
                    firestoreWrapper.subscription.data,
                    dataSources.subscription.allData,
                    dataSources.subscription::insertOrUpdate,
                    SubscriptionDocument::toSubscriptionOffline,
                )
            }
            launch {
                syncFromCloud(
                    firestoreWrapper.category.data,
                    dataSources.category.allData,
                    dataSources.category::insertOrUpdate,
                    CategoryDocument::toCategoryOffline,
                )
            }
            launch {
                syncFromCloud(
                    firestoreWrapper.card.data,
                    dataSources.card.allData,
                    dataSources.card::insertOrUpdate,
                    CardDocument::toCardOffline,
                )
            }
        }
        return Result.success()
    }

    private suspend fun <T : FirestoreDocumentSync, R : OfflineObject> syncFromCloud(
        cloudDataFlow: Flow<List<T>>,
        localDataFlow: Flow<List<R>>,
        localSync: suspend (R) -> Unit,
        transform: (T) -> R,
    ) {
        combine(
            cloudDataFlow,
            localDataFlow,
        ) { cloudData, localData ->
            cloudData.filter { cloud ->
                localData.none { it.cloudId == cloud.id }
            }
        }.first().fastForEach { data ->
            _mutex.withLock(data.id) {
                localSync(transform(data))
            }
        }
    }

    companion object {
        const val TAG = "InitialDataSyncWorker"
    }
}
