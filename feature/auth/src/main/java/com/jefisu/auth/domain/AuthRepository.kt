package com.jefisu.auth.domain

import com.jefisu.domain.util.Result

typealias OneMessageAuthResult = Result<AuthMessage.Success, AuthMessage.Error>
typealias EmptyAuthResult = Result<Unit, AuthMessage.Error>

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    ): EmptyAuthResult

    suspend fun signUp(
        email: String,
        password: String,
    ): EmptyAuthResult

    suspend fun sendPasswordResetEmail(email: String): OneMessageAuthResult
}
