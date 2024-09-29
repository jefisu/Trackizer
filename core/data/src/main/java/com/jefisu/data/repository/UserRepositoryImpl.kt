package com.jefisu.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.data.util.userFlow
import com.jefisu.domain.model.User
import com.jefisu.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(private val auth: FirebaseAuth = Firebase.auth) : UserRepository {

    override val user: Flow<User?> = auth.userFlow().map { firebaseUser ->
        if (firebaseUser == null) return@map null

        val alternativeName = "User ${firebaseUser.uid.filter { it.isDigit() }.take(8)}"
        User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: alternativeName,
            email = firebaseUser.email ?: "No email",
            pictureUrl = firebaseUser.photoUrl?.toString(),
        )
    }

    override fun isAuthenticated(): Boolean = Firebase.auth.currentUser != null

    override suspend fun signOut() {
        auth.signOut()
    }
}
