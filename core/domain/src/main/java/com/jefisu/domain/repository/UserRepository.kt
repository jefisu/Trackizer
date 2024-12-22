package com.jefisu.domain.repository

import com.jefisu.domain.model.User
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<User?>
    fun isAuthenticated(): Boolean
    suspend fun signOut()
    suspend fun deleteAccount(password: String): Result<Unit, DataMessage>
}