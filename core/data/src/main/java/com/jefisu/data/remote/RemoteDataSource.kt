package com.jefisu.data.remote

import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource<T> {
    val data: Flow<List<T>>
    suspend fun insert(obj: T): Result<String, DataMessage>
}
