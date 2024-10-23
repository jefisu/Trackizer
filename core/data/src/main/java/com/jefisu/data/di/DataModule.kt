package com.jefisu.data.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jefisu.data.StandardDispatcherProvider
import com.jefisu.data.local.DataSources
import com.jefisu.data.local.OfflineDataSource
import com.jefisu.data.local.model.CardOffline
import com.jefisu.data.local.model.CategoryOffline
import com.jefisu.data.local.model.PendingSync
import com.jefisu.data.local.model.SubscriptionOffline
import com.jefisu.data.local.model.SyncAction
import com.jefisu.data.mapper.toCard
import com.jefisu.data.mapper.toCardOffline
import com.jefisu.data.mapper.toCategory
import com.jefisu.data.mapper.toCategoryOffline
import com.jefisu.data.remote.FirestoreDataSource
import com.jefisu.data.remote.FirestoreDataSources
import com.jefisu.data.remote.document.CardDocument
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.data.remote.document.SubscriptionDocument
import com.jefisu.data.repository.DataRepositoryImpl
import com.jefisu.data.repository.DataSyncRepositoryImpl
import com.jefisu.data.repository.SettingsRepositoryImpl
import com.jefisu.data.repository.SubscriptionRepositoryImpl
import com.jefisu.data.repository.UserRepositoryImpl
import com.jefisu.data.worker.WorkerStarter
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.model.Card
import com.jefisu.domain.model.Category
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.DataSyncRepository
import com.jefisu.domain.repository.SettingsRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

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

    @Provides
    @Singleton
    fun provideUserRepository(realm: Realm): UserRepository = UserRepositoryImpl(
        realm = realm,
    )

    @Provides
    @Singleton
    fun provideSubscriptionRepository(
        dataSources: DataSources,
        categoryRepository: CategoryRepository,
        cardRepository: CardRepository,
    ): SubscriptionRepository = SubscriptionRepositoryImpl(
        dispatcher = StandardDispatcherProvider,
        dataSources = dataSources,
        categoryRepository = categoryRepository,
        cardRepository = cardRepository,
    )

    @Provides
    @Singleton
    fun provideCategoryRepository(dataSources: DataSources): CategoryRepository =
        DataRepositoryImpl(
            dataSource = dataSources.category,
            pendingSyncDataSource = dataSources.pendingSync,
            input = Category::toCategoryOffline,
            output = CategoryOffline::toCategory,
        )

    @Provides
    @Singleton
    fun provideCardRepository(dataSources: DataSources): CardRepository = DataRepositoryImpl(
        dataSource = dataSources.card,
        pendingSyncDataSource = dataSources.pendingSync,
        input = Card::toCardOffline,
        output = CardOffline::toCard,
    )

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context,
        dataStore: DataStore<Preferences>,
    ): SettingsRepository = SettingsRepositoryImpl(
        context = context,
        dataStore = dataStore,
        dispatcherProvider = StandardDispatcherProvider,
    )

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

    @Provides
    @Singleton
    fun provideWorkerStarter(app: Application) = WorkerStarter(app)

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = StandardDispatcherProvider

    @Provides
    @Singleton
    fun provideDataSyncRepository(
        workerStarter: WorkerStarter,
        dataSources: DataSources,
        firestoreDataSources: FirestoreDataSources,
        settingsRepository: SettingsRepository,
    ): DataSyncRepository = DataSyncRepositoryImpl(
        workerStarter = workerStarter,
        dispatcherProvider = StandardDispatcherProvider,
        offlineDataSources = dataSources,
        firestoreDataSources = firestoreDataSources,
        settingsRepository = settingsRepository,
    )
}

val Context.dataStore by preferencesDataStore("app_pref")
