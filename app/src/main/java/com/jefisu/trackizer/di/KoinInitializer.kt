package com.jefisu.trackizer.di

import android.content.Context
import com.jefisu.authentication.di.authModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class KoinInitializer(
    private val context: Context
) {
    fun init() {
        startKoin {
            androidContext(context)
            androidLogger()
            modules(authModule)
        }
    }
}