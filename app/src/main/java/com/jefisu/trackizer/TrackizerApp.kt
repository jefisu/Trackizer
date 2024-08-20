package com.jefisu.trackizer

import android.app.Application
import com.google.firebase.FirebaseApp

class TrackizerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
