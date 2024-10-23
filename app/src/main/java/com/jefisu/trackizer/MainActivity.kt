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
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.FlashMessageDialog
import com.jefisu.designsystem.util.AppConfig
import com.jefisu.designsystem.util.LocalAppConfig
import com.jefisu.domain.repository.DataSyncRepository
import com.jefisu.domain.repository.SettingsRepository
import com.jefisu.domain.repository.UserRepository
import com.jefisu.trackizer.navigation.AppNavHost
import com.jefisu.ui.MessageController
import com.jefisu.ui.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var dataSyncRepository: DataSyncRepository

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setSystemBarColor()
        setPortraitOrientationOnly()
        setupDefaultSettings()
        setObserveDataSync()
        setContent {
            val message by MessageController.message.collectAsStateWithLifecycle()
            TrackizerTheme {
                CompositionLocalProvider(
                    LocalAppConfig provides appConfig(),
                ) {
                    FlashMessageDialog(
                        message = message,
                        onDismiss = MessageController::closeMessage,
                    )

                    AppNavHost(navigator = navigator)
                }
            }
        }
    }

    private fun setSystemBarColor() {
        val view = window.decorView
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun setPortraitOrientationOnly() {
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun setupDefaultSettings() {
        lifecycleScope.launch {
            settingsRepository.setDefaultSettings()
        }
    }

    @Composable
    private fun appConfig(): AppConfig {
        val appConfig by combine(
            settingsRepository.settings,
            userRepository.user,
        ) { settings, user ->
            AppConfig(
                settings = settings,
                user = user,
            )
        }.collectAsStateWithLifecycle(
            initialValue = AppConfig(),
        )
        return appConfig
    }

    private fun setObserveDataSync() {
        lifecycleScope.launch {
            dataSyncRepository.observeDataStoreChanges()
        }
    }
}
