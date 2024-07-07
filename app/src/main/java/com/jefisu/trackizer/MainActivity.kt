package com.jefisu.trackizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jefisu.trackizer.navigation.NavGraph
import com.jefisu.ui.theme.AppTheme
import com.jefisu.welcome.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            AppTheme(modifier = Modifier.fillMaxSize()) {
                NavGraph(startDestination = WelcomeScreen)
            }
        }
    }
}
