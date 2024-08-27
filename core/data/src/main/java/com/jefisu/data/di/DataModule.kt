package com.jefisu.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.jefisu.data.repository.CardRepositoryImpl
import com.jefisu.data.repository.SubscriptionRepositoryImpl
import com.jefisu.data.repository.UserRepositoryImpl
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.SubscriptionRepository
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

    @Provides
    @Singleton
    fun provideSubscriptionRepository(): SubscriptionRepository = SubscriptionRepositoryImpl()

    @Provides
    @Singleton
    fun provideCardRepository(): CardRepository = CardRepositoryImpl()
}

val Context.dataStore by preferencesDataStore("user_pref")
