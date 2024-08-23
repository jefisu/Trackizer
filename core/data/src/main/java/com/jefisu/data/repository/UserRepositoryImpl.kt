package com.jefisu.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositoryImpl(private val dataStore: DataStore<Preferences>) : UserRepository {

    private val emailKey = stringPreferencesKey("email")

    override val email: Flow<String?> = dataStore.data.map { it[emailKey] }

    override suspend fun rememberEmail(email: String) {
        dataStore.edit { it[emailKey] = email }
    }

    override suspend fun forgetEmail() {
        dataStore.edit { it.clear() }
    }

    override fun isAuthenticated(): Boolean = Firebase.auth.currentUser != null
}
