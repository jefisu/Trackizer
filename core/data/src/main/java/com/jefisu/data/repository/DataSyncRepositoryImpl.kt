package com.jefisu.data.repository

import com.jefisu.data.local.DataSources
import com.jefisu.data.util.safeCall
import com.jefisu.data.worker.SyncWorker
import com.jefisu.data.worker.WorkerStarter
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.repository.DataSyncRepository
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.transform

class DataSyncRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val workerStarter: WorkerStarter,
    private val dataSources: DataSources,
) : DataSyncRepository {
    override suspend fun observeDataStoreChanges() {
        safeCall(
            dispatcher = dispatcherProvider.io,
            causedException = "observeDataStoreChanges",
        ) {
            dataSources.pendingSync
                .allData
                .distinctUntilChanged()
                .transform {
                    emit(it.filter { !it.isSynced })
                }
                .collect {
                    if (it.isNotEmpty()) {
                        workerStarter.startOnce(SyncWorker::class.java, SyncWorker.TAG)
                    }
                }
        }
    }
}
