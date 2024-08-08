package com.jefisu.trackizer

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jefisu.auth.di.authModule
import com.jefisu.data.di.dataModule
import com.jefisu.home.di.homeModule
import com.jefisu.spending_budgets.di.spendingBudgetsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TrackizerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidContext(this@TrackizerApp)
            androidLogger()
            modules(
                authModule,
                dataModule,
                homeModule,
                spendingBudgetsModule,
            )
        }
    }
}
