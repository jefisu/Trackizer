package com.jefisu.trackizer

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.auth.di.authModule
import com.jefisu.data.di.userModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class TrackizerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        initKoin()
    }

    private fun initKoin() = startKoin {
        androidContext(this@TrackizerApp)
        androidLogger()
        modules(
            appModule,
            authModule,
            userModule
        )
    }

    private val appModule = module {
        single { Firebase.auth }
    }
}