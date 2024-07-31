package com.jefisu.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.jefisu.data.repository.UserRepositoryImpl
import com.jefisu.domain.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userModule = module {
    single { get<Context>().dataStore }
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
}

private val Context.dataStore by preferencesDataStore(
    name = "user_prefs",
)
