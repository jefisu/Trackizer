package com.jefisu.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerLogoBox
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.navigation.LocalNavigator
import com.jefisu.ui.screen.LocalScreenIsSmall
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen() {
    val scope = rememberCoroutineScope()
    val navigator = LocalNavigator.current

    WelcomeScreenContent(
        onNavigate = {
            scope.launch { navigator.navigate(it) }
        },
    )
}

@Composable
private fun WelcomeScreenContent(onNavigate: (Destination) -> Unit) {
    val isSmallScreen = LocalScreenIsSmall.current
    TrackizerLogoBox {
        Image(
            painter = painterResource(id = R.drawable.welcome_asset),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp * if (isSmallScreen) 0.6f else 1f),
        )
        Image(
            painter = painterResource(id = R.drawable.shapes_to_blur),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
                .blur(100.dp)
                .rotate(15f),
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .safeDrawingPadding()
                .padding(TrackizerTheme.spacing.extraMedium),
        ) {
            Text(
                text = stringResource(R.string.congue_malesuada_in),
                textAlign = TextAlign.Center,
                style = TrackizerTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(40.dp))
            TrackizerButton(
                text = stringResource(R.string.get_started),
                type = ButtonType.Primary,
                onClick = {
                    onNavigate(Destination.AuthScreen(isLogging = false))
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
            TrackizerButton(
                text = stringResource(R.string.i_have_an_account),
                type = ButtonType.Secondary,
                onClick = {
                    onNavigate(Destination.AuthScreen(isLogging = true))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    TrackizerTheme {
        WelcomeScreenContent(
            onNavigate = {},
        )
    }
}