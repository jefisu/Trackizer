@file:OptIn(InternalSerializationApi::class)

package com.jefisu.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.jefisu.data.local.DataSources
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.mapper.toCardOffline
import com.jefisu.data.mapper.toCategoryOffline
import com.jefisu.data.mapper.toSubscriptionOffline
import com.jefisu.data.remote.DataChange
import com.jefisu.data.remote.document.CardDocument
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.data.remote.document.FirestoreDocumentSync
import com.jefisu.data.remote.document.SubscriptionDocument
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json

@HiltWorker
class FirestoreSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val offlineDataSources: DataSources,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val dataChange = getDataChange() ?: return Result.failure()

        dataChange
            .onAdded { it.addToLocalDatabase() }
            .onModified { it.addToLocalDatabase() }
            .onRemoved { it.removeFromLocalDatabase() }

        return Result.success()
    }

    private fun getDataChange(): DataChange? {
        val dataChangeJson = inputData.getString(DATA_CHANGE_KEY) ?: return null
        val dataChange = Json.decodeFromString<DataChange>(dataChangeJson)
        return dataChange
    }

    private suspend fun FirestoreDocumentSync.addToLocalDatabase() {
        with(offlineDataSources) {
            when (this@addToLocalDatabase) {
                is SubscriptionDocument -> subscription.insertOrUpdate(toSubscriptionOffline())
                is CategoryDocument -> category.insertOrUpdate(toCategoryOffline())
                is CardDocument -> card.insertOrUpdate(toCardOffline())
            }
        }
    }

    private suspend fun FirestoreDocumentSync.removeFromLocalDatabase() {
        val offline = when (this@removeFromLocalDatabase) {
            is SubscriptionDocument -> offlineDataSources.subscription.getById(id)
            is CategoryDocument -> offlineDataSources.category.getById(id)
            is CardDocument -> offlineDataSources.card.getById(id)
        }.also {
            if (it == null) return
        }

        with(offlineDataSources) {
            when (offline) {
                is SubscriptionOffline -> subscription.delete(offline)
                is CategoryOffline -> category.delete(offline)
                is CardOffline -> card.delete(offline)
                else -> Unit
            }
        }
    }

    companion object {
        const val DATA_CHANGE_KEY = "DATA_CHANGE_KEY"
    }
}