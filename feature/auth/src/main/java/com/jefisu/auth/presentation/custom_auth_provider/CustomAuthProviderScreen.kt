package com.jefisu.auth.presentation.custom_auth_provider

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.auth.R
import com.jefisu.designsystem.FacebookColor
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography

@Composable
fun CustomAuthProviderRoot(navigateToRegister: () -> Unit) {
    CustomAuthProviderScreen(
        navigateToRegister = navigateToRegister,
    )
}

@Composable
internal fun CustomAuthProviderScreen(navigateToRegister: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(TrackizerTheme.spacing.extraMedium),
    ) {
        TrackizerButton(
            text = stringResource(id = R.string.sign_up_with, "Google"),
            type = ButtonType.Dynamic(
                container = Color.White,
                content = Gray80,
            ),
            leadingIconRes = R.drawable.ic_google,
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.medium))
        TrackizerButton(
            text = stringResource(id = R.string.sign_up_with, "Facebook"),
            type = ButtonType.Dynamic(
                container = FacebookColor,
                content = Color.White,
            ),
            leadingIconRes = R.drawable.ic_facebook,
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.large))
        Text(
            text = stringResource(R.string.or),
            style = TrackizerTheme.typography.headline2,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.large))
        TrackizerButton(
            text = stringResource(id = R.string.sign_up_with, "E-mail"),
            type = ButtonType.Secondary,
            onClick = navigateToRegister,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        Text(
            text = stringResource(R.string.by_registering_you_agree),
            style = TrackizerTheme.typography.bodySmall,
            color = Gray50,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun CustomAuthProviderScreenPreview() {
    TrackizerTheme {
        CustomAuthProviderScreen(
            navigateToRegister = {},
        )
    }
}
