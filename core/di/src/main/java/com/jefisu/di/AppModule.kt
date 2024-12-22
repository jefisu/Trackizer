package com.jefisu.di

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.jefisu.data.StandardDispatcherProvider
import com.jefisu.data.worker.WorkerStarter
import com.jefisu.domain.DispatcherProvider
import com.jefisu.domain.repository.UserRepository
import com.jefisu.ui.navigation.DefaultNavigator
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore by preferencesDataStore("app_pref")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNavigator(userRepository: UserRepository): Navigator {
        val startDestination = if (userRepository.isAuthenticated()) {
            Destination.AuthenticatedGraph
        } else {
            Destination.AuthGraph
        }
        return DefaultNavigator(startDestination)
    }

    @Provides
    @Singleton
    fun provideDataStore(app: Application) = app.dataStore

    @Provides
    @Singleton
    fun provideWorkerStarter(app: Application) = WorkerStarter(app)

    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = StandardDispatcherProvider
}