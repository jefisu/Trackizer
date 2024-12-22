package com.jefisu.di

import com.jefisu.data.repository.CardRepositoryImpl
import com.jefisu.data.repository.CategoryRepositoryImpl
import com.jefisu.data.repository.DataSyncRepositoryImpl
import com.jefisu.data.repository.SettingsRepositoryImpl
import com.jefisu.data.repository.SubscriptionRepositoryImpl
import com.jefisu.data.repository.UserRepositoryImpl
import com.jefisu.domain.repository.CardRepository
import com.jefisu.domain.repository.CategoryRepository
import com.jefisu.domain.repository.DataSyncRepository
import com.jefisu.domain.repository.SettingsRepository
import com.jefisu.domain.repository.SubscriptionRepository
import com.jefisu.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindSettingsRepository(repository: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindSubscriptionRepository(
        repository: SubscriptionRepositoryImpl,
    ): SubscriptionRepository

    @Binds
    abstract fun bindCategoryRepository(repository: CategoryRepositoryImpl): CategoryRepository

    @Binds
    abstract fun bindCardRepository(repository: CardRepositoryImpl): CardRepository

    @Binds
    abstract fun bindDataSyncRepository(repository: DataSyncRepositoryImpl): DataSyncRepository
}