package com.jefisu.trackizer.di

import com.jefisu.domain.repository.UserRepository
import com.jefisu.ui.navigation.DefaultNavigator
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

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
}
