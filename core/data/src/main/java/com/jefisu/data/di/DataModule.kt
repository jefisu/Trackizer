package com.jefisu.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.jefisu.data.repository.UserRepositoryImpl
import com.jefisu.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideUserRepository(@ApplicationContext context: Context): UserRepository =
        UserRepositoryImpl(
            dataStore = context.dataStore,
        )
}

val Context.dataStore by preferencesDataStore("user_pref")
