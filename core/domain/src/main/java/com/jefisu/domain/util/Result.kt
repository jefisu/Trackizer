package com.jefisu.common.util

sealed interface Result<out D, out M : Message> {
    data class Success<out D, out M : Message>(val data: D) : Result<D, M>
    data class Error<out M : Message>(val message: M) : Result<Nothing, M>
}

inline fun <T, M : Message> Result<T, M>.onSuccess(action: (T) -> Unit): Result<T, M> {
    return when (this) {
        is Result.Error -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, M : Message> Result<T, M>.onError(action: (M) -> Unit): Result<T, M> {
    return when (this) {
        is Result.Error -> {
            action(message)
            this
        }

        is Result.Success -> this
    }
}
