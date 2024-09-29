package com.jefisu.data.util

import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> safeCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    causedException: String? = null,
    action: suspend CoroutineScope.() -> T,
): T? = withContext(dispatcher) {
    try {
        action()
    } catch (e: Exception) {
        println("$causedException - Exception: ${e.message}")
        null
    }
}

suspend fun <T> safeCallResult(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    causedException: String? = null,
    exceptions: Map<KClass<out Exception>, DataMessage> = emptyMap(),
    action: suspend CoroutineScope.() -> T,
): Result<T, DataMessage> = withContext(dispatcher) {
    try {
        Result.Success(action())
    } catch (e: Exception) {
        println("$causedException - Exception: ${e.message}")
        val message = exceptions[e::class] ?: DataMessage.UNKNOWN_ERROR
        Result.Error(message)
    }
}
