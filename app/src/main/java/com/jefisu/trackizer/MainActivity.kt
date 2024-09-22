package com.jefisu.trackizer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.FlashMessageDialog
import com.jefisu.designsystem.util.AppConfig
import com.jefisu.designsystem.util.LocalAppConfig
import com.jefisu.domain.repository.SettingsRepository
import com.jefisu.domain.repository.UserRepository
import com.jefisu.home.presentation.HomeScreen
import com.jefisu.trackizer.navigation.AppNavHost
import com.jefisu.ui.MessageController
import com.jefisu.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        systemBarColor()
        setPortraitOrientationOnly()
        setDefaultSettings()
        setContent {
            val navController = rememberNavController()
            val message by MessageController.message.collectAsStateWithLifecycle()
            TrackizerTheme {
                CompositionLocalProvider(
                    LocalAppConfig provides appConfig(),
                ) {
                    FlashMessageDialog(
                        message = message,
                        onDismiss = MessageController::closeMessage,
                    )

                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination(),
                    )
                }
            }
        }
    }

    private fun systemBarColor() {
        val view = window.decorView
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    private fun startDestination(): Any =
        if (userRepository.isAuthenticated()) HomeScreen else WelcomeScreen

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setPortraitOrientationOnly() {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setDefaultSettings() {
        lifecycleScope.launch {
            settingsRepository.setDefaultSettings()
        }
    }

    @Composable
    private fun appConfig(): AppConfig {
        val settings by settingsRepository.settings.collectAsStateWithLifecycle()
        val user by userRepository.user.collectAsStateWithLifecycle(initialValue = null)
        return AppConfig(
            settings = settings,
            user = user,
        )
    }
}
