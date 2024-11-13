package com.jefisu.domain.repository

import com.jefisu.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<User?>
    fun isAuthenticated(): Boolean
    suspend fun signOut()
}
