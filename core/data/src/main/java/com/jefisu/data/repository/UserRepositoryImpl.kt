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
        firebaseUser?.let { user ->
            val id = user.uid
            val name = user.displayName.let {
                val alternativeName = "User ${id.filter { it.isDigit() }.take(8)}"
                if (it.isNullOrEmpty()) alternativeName else it
            }
            User(
                id = id,
                name = name,
                email = user.email ?: "No email",
                pictureUrl = user.photoUrl?.toString(),
            )
        } ?: return@map null
    }

    override fun isAuthenticated(): Boolean = Firebase.auth.currentUser != null

    override suspend fun signOut() {
        auth.signOut()
    }
}
