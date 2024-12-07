package com.jefisu.data.repository

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.data.util.safeCallResult
import com.jefisu.data.util.userFlow
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.User
import com.jefisu.domain.repository.UserRepository
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.Result
import io.realm.kotlin.Realm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val realm: Realm,
    private val dispatcherProvider: DispatcherProvider,
) : UserRepository {

    private val auth = Firebase.auth

    override val user: Flow<User?> = auth.userFlow().map { firebaseUser ->
        if (firebaseUser == null) return@map null
        User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName.orEmpty(),
            email = firebaseUser.email ?: "No email",
            pictureUrl = firebaseUser.photoUrl?.toString(),
        )
    }

    override fun isAuthenticated(): Boolean = Firebase.auth.currentUser != null

    override suspend fun signOut() {
        auth.signOut()
        realm.write { deleteAll() }
    }

    override suspend fun deleteAccount(password: String): Result<Unit, DataMessage> {
        return safeCallResult(
            dispatcher = dispatcherProvider.io,
            exceptions = mapOf(
                FirebaseAuthInvalidCredentialsException::class to DataMessage.INCORRECT_PASSWORD,
                Exception::class to DataMessage.DELETE_ACCOUNT_FAILED,
            ),
        ) {
            val authResult = auth.signInWithEmailAndPassword(
                auth.currentUser?.email.orEmpty(),
                password,
            ).await()
            requireNotNull(authResult.user)

            auth.currentUser?.delete()?.await()
            launch { realm.write { deleteAll() } }
        }
    }
}
