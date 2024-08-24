package com.jefisu.trackizer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.domain.repository.UserRepository
import com.jefisu.home.presentation.HomeScreen
import com.jefisu.trackizer.navigation.AppNavHost
import com.jefisu.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        systemBarColor()
        setContent {
            val navController = rememberNavController()
            TrackizerTheme {
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination(),
                )
            }
        }
    }

    private fun systemBarColor() {
        val view = window.decorView
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    private fun startDestination(): Any =
        if (userRepository.isAuthenticated()) HomeScreen else WelcomeScreen
}
