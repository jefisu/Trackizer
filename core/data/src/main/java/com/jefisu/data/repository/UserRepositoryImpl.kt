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

internal class UserRepositoryImpl(private val dataStore: DataStore<Preferences>) : UserRepository {

    private val auth = Firebase.auth
    private val emailKey = stringPreferencesKey("email")

    override val emailFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[emailKey]
    }

    override suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[emailKey] = email
        }
    }

    override suspend fun deleteSavedEmail() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override fun isAuthenticated(): Boolean = auth.currentUser != null
}
