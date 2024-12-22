package com.jefisu.di

import com.jefisu.data.remote.FirestoreDataSource
import com.jefisu.data.remote.FirestoreDataSources
import com.jefisu.data.remote.document.CardDocument
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.data.remote.document.SubscriptionDocument
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourcesModule {

    @Provides
    @Singleton
    fun provideFirestoreSyncWrapper() = FirestoreDataSources(
        subscription = FirestoreDataSource(
            collectionName = "subscriptions",
            clazz = SubscriptionDocument::class.java,
        ),
        category = FirestoreDataSource(
            collectionName = "categories",
            clazz = CategoryDocument::class.java,
        ),
        card = FirestoreDataSource(
            collectionName = "cards",
            clazz = CardDocument::class.java,
        ),
    )
}