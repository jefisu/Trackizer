package com.jefisu.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class WorkerStarter(context: Context) {

    private val _workManager = WorkManager.getInstance(context)

    fun addToQueue(
        clazz: Class<out CoroutineWorker>,
        inputData: Data? = null,
    ) {
        val request = buildRequest(clazz, inputData)
        _workManager.enqueue(request)
    }

    private fun buildRequest(
        clazz: Class<out CoroutineWorker>,
        inputData: Data? = null,
    ): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequest.Builder(clazz)
            .setConstraints(constraints)
            .setInputData(inputData ?: Data.EMPTY)
            .build()
    }
}