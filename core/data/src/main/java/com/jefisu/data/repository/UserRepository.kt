package com.jefisu.data.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val emailFlow: Flow<String?>
    suspend fun saveEmail(email: String)
    suspend fun deleteSavedEmail()
    fun isAuthenticated(): Boolean
}