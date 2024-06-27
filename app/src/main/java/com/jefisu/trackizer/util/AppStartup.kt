package com.jefisu.trackizer.util

import android.content.Context
import com.jefisu.trackizer.di.KoinInitializer

class AppStartup(
    private val context: Context
) {
    fun init() {
        KoinInitializer(context).init()
    }
}