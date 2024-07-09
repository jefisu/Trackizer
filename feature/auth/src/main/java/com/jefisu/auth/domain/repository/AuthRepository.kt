package com.jefisu.auth.domain.repository

import com.jefisu.auth.data.AuthMessage
import com.jefisu.common.util.Result

typealias EmptyAuthResult = Result<Unit, AuthMessage.Error>
typealias OneMessageAuthResult = Result<AuthMessage.Success, AuthMessage.Error>

interface AuthRepository {
    suspend fun signIn(email: String, password: String): EmptyAuthResult
    suspend fun signUp(email: String, password: String): EmptyAuthResult
    suspend fun sendPasswordResetEmail(email: String): OneMessageAuthResult
}