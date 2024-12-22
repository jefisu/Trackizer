package com.jefisu.di

import com.jefisu.data.local.DataSources
import com.jefisu.data.local.OfflineDataSource
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.PendingSync
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.local.model.SyncAction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourcesModule {

    @Provides
    @Singleton
    fun provideRealm(): Realm {
        val schema = setOf(
            SubscriptionOffline::class,
            CategoryOffline::class,
            CardOffline::class,
            PendingSync::class,
            SyncAction::class,
        )
        val config = RealmConfiguration.Builder(schema)
            .compactOnLaunch()
            .build()
        return Realm.open(config)
    }

    @Provides
    @Singleton
    fun provideDataSources(realm: Realm) = DataSources(
        subscription = OfflineDataSource(
            clazz = SubscriptionOffline::class,
            realm = realm,
        ),
        category = OfflineDataSource(
            clazz = CategoryOffline::class,
            realm = realm,
        ),
        card = OfflineDataSource(
            clazz = CardOffline::class,
            realm = realm,
        ),
        pendingSync = OfflineDataSource(
            clazz = PendingSync::class,
            realm = realm,
        ),
    )
}