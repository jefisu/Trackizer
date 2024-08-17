package com.jefisu.trackizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.jefisu.designsystem.TrackizerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        systemBarColor()
        setContent {
            TrackizerTheme {
                Box(modifier = Modifier.fillMaxSize())
            }
        }
    }

    private fun systemBarColor() {
        val view = window.decorView
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }
}
