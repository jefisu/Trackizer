package com.jefisu.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val auth: FirebaseAuth
) : UserRepository {

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

    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }
}