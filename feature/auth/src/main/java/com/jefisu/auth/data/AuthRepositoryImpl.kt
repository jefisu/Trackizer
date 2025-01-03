package com.jefisu.auth.data

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.auth.domain.AuthMessage
import com.jefisu.auth.domain.AuthRepository
import com.jefisu.auth.domain.EmptyAuthResult
import com.jefisu.auth.domain.OneMessageAuthResult
import com.jefisu.domain.util.Result
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@ViewModelScoped
class AuthRepositoryImpl @Inject constructor() : AuthRepository {

    private val auth = Firebase.auth

    override suspend fun signIn(
        email: String,
        password: String,
    ): EmptyAuthResult = runCatch {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(
        email: String,
        password: String,
    ): EmptyAuthResult = runCatch {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendPasswordResetEmail(email: String): OneMessageAuthResult = runCatch {
        auth.sendPasswordResetEmail(email).await()
        AuthMessage.Success.SENT_PASSWORD_RESET_EMAIL
    }

    private suspend fun <T> runCatch(block: suspend () -> T): Result<T, AuthMessage.Error> =
        withContext(Dispatchers.IO) {
            try {
                Result.Success(block())
            } catch (_: FirebaseAuthInvalidCredentialsException) {
                Result.Error(AuthMessage.Error.INVALID_EMAIL_OR_PASSWORD)
            } catch (_: FirebaseAuthUserCollisionException) {
                Result.Error(AuthMessage.Error.USER_ALREADY_EXISTS)
            } catch (_: FirebaseAuthInvalidUserException) {
                Result.Error(AuthMessage.Error.USER_NOT_FOUND)
            } catch (_: FirebaseNetworkException) {
                Result.Error(AuthMessage.Error.INTERNET_UNAVAILABLE)
            } catch (_: FirebaseException) {
                Result.Error(AuthMessage.Error.SERVER_ERROR)
            }
        }
}