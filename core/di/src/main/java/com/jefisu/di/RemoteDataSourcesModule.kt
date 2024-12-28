package com.jefisu.di

import com.jefisu.data.remote.FirestoreDataSource
import com.jefisu.data.remote.FirestoreDataSources
import com.jefisu.data.remote.document.CardDocument
import com.jefisu.data.remote.document.CategoryDocument
import com.jefisu.data.remote.document.SubscriptionDocument
import com.jefisu.data.remote.image.ImageUploader
import com.jefisu.data.remote.image.ImageUploaderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import javax.inject.Singleton
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourcesModule {

    @Binds
    abstract fun bindImageUploader(uploader: ImageUploaderImpl): ImageUploader

    companion object {
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
        fun provideHttpClient(): HttpClient {
            return HttpClient(CIO) {
                expectSuccess = true
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            prettyPrint = true
                            isLenient = true
                        },
                    )
                }
                install(Logging) {
                    level = LogLevel.ALL
                }
            }
        }
    }
}