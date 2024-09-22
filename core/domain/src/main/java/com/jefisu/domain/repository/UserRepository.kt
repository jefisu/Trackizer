package com.jefisu.domain.repository

import com.jefisu.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<User?>
    suspend fun updateUser(user: User)
    fun isAuthenticated(): Boolean
    suspend fun signOut()
}
