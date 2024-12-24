@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.trackizer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.FlashMessageDialog
import com.jefisu.designsystem.util.LocalSettings
import com.jefisu.trackizer.navigation.AppNavHost
import com.jefisu.ui.navigation.Navigator
import com.jefisu.ui.sharedtransition.LocalSharedTransitionScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setSystemBarColor()
        setPortraitOrientationOnly()
        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            TrackizerTheme {
                SharedTransitionLayout {
                    CompositionLocalProvider(
                        LocalSettings provides state.settings,
                        LocalSharedTransitionScope provides this,
                    ) {
                        FlashMessageDialog(
                            message = state.message,
                            onDismiss = viewModel::closeMessage,
                        )
                        AppNavHost(navigator = navigator)
                    }
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
}