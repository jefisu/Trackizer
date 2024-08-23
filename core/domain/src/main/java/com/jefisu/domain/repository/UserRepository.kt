package com.jefisu.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val email: Flow<String?>
    suspend fun rememberEmail(email: String)
    suspend fun forgetEmail()
    fun isAuthenticated(): Boolean
}
