package com.jefisu.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class WorkerStarter(context: Context) {

    private val _workManager = WorkManager.getInstance(context)

    fun startOnce(
        clazz: Class<out CoroutineWorker>,
        tag: String,
    ) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequest.Builder(clazz)
            .setConstraints(constraints)
            .build()

        _workManager.enqueueUniqueWork(
            tag,
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }
}
