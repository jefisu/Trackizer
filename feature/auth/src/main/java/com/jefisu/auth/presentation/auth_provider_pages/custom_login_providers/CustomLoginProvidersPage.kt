package com.jefisu.auth.presentation.auth_provider_pages.custom_login_providers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jefisu.auth.R
import com.jefisu.auth.data.AuthMessage
import com.jefisu.auth.presentation.auth_provider_pages.custom_login_providers.components.FacebookButton
import com.jefisu.auth.presentation.auth_provider_pages.custom_login_providers.components.GoogleButton
import com.jefisu.ui.components.Button
import com.jefisu.ui.components.ButtonProperties
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Theme

@Composable
fun CustomLoginProvidersPage(
    onNavigateToRegisterEmailPage: () -> Unit,
    onNavigateToHome: () -> Unit,
    onShowError: (AuthMessage.Error) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GoogleButton(
            onSuccessfulLogin = onNavigateToHome,
            onFailureLogin = onShowError,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        FacebookButton(
            onSuccessfulLogin = onNavigateToHome,
            onFailureLogin = onShowError,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(Theme.spacing.large))
        Text(
            text = stringResource(R.string.or),
            style = Theme.typography.headline2,
        )
        Spacer(modifier = Modifier.height(Theme.spacing.large))
        Button(
            text = stringResource(id = R.string.sign_up_with, "E-mail"),
            properties = ButtonProperties(
                type = ButtonType.Secondary,
            ),
            onClick = onNavigateToRegisterEmailPage,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
        Text(
            text = stringResource(R.string.by_registering_you_agree),
            style = Theme.typography.bodySmall,
            color = Gray50,
            textAlign = TextAlign.Center,
        )
    }
}
