package com.jefisu.data.repository

import androidx.compose.ui.util.fastForEach
import androidx.work.Data
import com.jefisu.data.local.DataSources
import com.jefisu.data.remote.FirestoreDataSources
import com.jefisu.data.worker.FirestoreSyncWorker
import com.jefisu.data.worker.OfflineSyncWorker
import com.jefisu.data.worker.WorkerStarter
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.repository.DataSyncRepository
import com.jefisu.domain.repository.SettingsRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Singleton
class DataSyncRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val workerStarter: WorkerStarter,
    private val offlineDataSources: DataSources,
    private val firestoreDataSources: FirestoreDataSources,
    private val settingsRepository: SettingsRepository,
) : DataSyncRepository {

    override suspend fun observeDataStoreChanges() {
        withContext(dispatcherProvider.io) {
            launch {
                pendingSyncObserver()
            }
            launch {
                remoteDataChangesObserver()
            }
        }
    }

    private suspend fun pendingSyncObserver() {
        val dataFlow = combine(
            offlineDataSources.pendingSync.allData,
            settingsRepository.settings,
        ) { pendingSync, settings ->
            if (!settings.isCloudSyncEnabled) {
                return@combine emptyList()
            }
            pendingSync
        }

        dataFlow.collect {
            it.fastForEach { pendingSync ->
                val data = Data.Builder()
                    .putString(
                        OfflineSyncWorker.PENDING_SYNC_ID_KEY,
                        pendingSync._id.toHexString(),
                    )
                    .build()
                workerStarter.addToQueue(OfflineSyncWorker::class.java, data)
            }
        }
    }

    private suspend fun remoteDataChangesObserver() {
        val remoteDataChanges = with(firestoreDataSources) {
            merge(
                subscription.dataChanges,
                category.dataChanges,
                card.dataChanges,
            ).distinctUntilChanged()
        }

        val dataChangesAvailable = combine(
            settingsRepository.settings,
            remoteDataChanges,
        ) { settings, dataChanges ->
            if (!settings.isCloudSyncEnabled) {
                return@combine emptyList()
            }
            dataChanges
        }

        dataChangesAvailable.collect { dataChanges ->
            dataChanges.fastForEach {
                val inputData = Data.Builder()
                    .putString(FirestoreSyncWorker.DATA_CHANGE_KEY, Json.encodeToString(it))
                    .build()
                workerStarter.addToQueue(FirestoreSyncWorker::class.java, inputData)
            }
        }
    }
}
