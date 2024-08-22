package com.jefisu.auth.domain

import com.jefisu.domain.util.EmptyResult
import com.jefisu.domain.util.Result

typealias OneMessageAuthResult = Result<AuthMessage.Success, AuthMessage.Error>

interface AuthRepository {
    suspend fun signIn(
        email: String,
        password: String,
    ): EmptyResult
}
