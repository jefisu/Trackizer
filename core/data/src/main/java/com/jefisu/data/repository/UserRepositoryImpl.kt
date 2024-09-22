package com.jefisu.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.domain.model.User
import com.jefisu.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class UserRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth = Firebase.auth,
) : UserRepository {

    private val _userKey = stringPreferencesKey("user")

    override val user: Flow<User?> = dataStore.data.map {
        val userJson = it[_userKey] ?: return@map null
        Json.decodeFromString<User>(userJson)
    }

    override suspend fun updateUser(user: User) {
        dataStore.edit {
            it[_userKey] = Json.encodeToString(user)
        }
    }

    override fun isAuthenticated(): Boolean = Firebase.auth.currentUser != null

    override suspend fun signOut() {
        auth.signOut()
        dataStore.edit { it.remove(_userKey) }
    }
}
