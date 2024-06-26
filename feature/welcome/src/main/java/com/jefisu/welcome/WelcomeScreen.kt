package com.jefisu.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.DynamicButton
import com.jefisu.ui.components.StandardScreenWithLogo
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Theme

@Composable
fun WelcomeScreen(
    onNavigateToRegisterScreen: () -> Unit,
    onNavigateToLoginScreen: () -> Unit
) {
    StandardScreenWithLogo {
        Image(
            painter = painterResource(id = R.drawable.welcome_asset),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.shapes_to_blur),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 120.dp)
                .blur(100.dp)
                .alpha(1f)
                .rotate(15f)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(Theme.spacing.extraMedium)
        ) {
            Text(
                text = stringResource(R.string.congue_malesuada_in),
                textAlign = TextAlign.Center,
                style = Theme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(40.dp))
            DynamicButton(
                text = stringResource(R.string.get_started),
                buttonType = ButtonType.Primary,
                onClick = onNavigateToRegisterScreen
            )
            Spacer(modifier = Modifier.height(Theme.spacing.medium))
            DynamicButton(
                text = stringResource(R.string.i_have_an_account),
                buttonType = ButtonType.Secondary,
                onClick = onNavigateToLoginScreen
            )
        }
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    AppTheme {
        WelcomeScreen(
            onNavigateToRegisterScreen = {},
            onNavigateToLoginScreen = {}
        )
    }
}