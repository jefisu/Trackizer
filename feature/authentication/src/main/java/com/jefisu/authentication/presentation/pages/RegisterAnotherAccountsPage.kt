package com.jefisu.authentication.presentation.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jefisu.authentication.R
import com.jefisu.authentication.presentation.AuthAction
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.DynamicButton
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Gray80
import com.jefisu.ui.theme.Theme
import com.jefisu.ui.theme.facebookColor

@Composable
fun RegisterAnotherAccountsPage(
    onAction: (AuthAction) -> Unit,
    onNavigateToRegisterEmailPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DynamicButton(
            text = stringResource(id = R.string.sign_up_with, "Google"),
            buttonType = ButtonType.DynamicColor(
                containerColor = Color.White,
                contentColor = Gray80
            ),
            leadingIconRes = R.drawable.ic_google,
            onClick = {
                onAction(AuthAction.SignUpWithGoogle)
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        DynamicButton(
            text = stringResource(id = R.string.sign_up_with, "Facebook"),
            buttonType = ButtonType.DynamicColor(
                containerColor = facebookColor,
                contentColor = Color.White
            ),
            leadingIconRes = R.drawable.ic_facebook,
            onClick = {
                onAction(AuthAction.SignUpWithFacebook)
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.large))
        Text(
            text = stringResource(R.string.or),
            style = Theme.typography.headline2
        )
        Spacer(modifier = Modifier.height(Theme.spacing.large))
        DynamicButton(
            text = stringResource(id = R.string.sign_up_with, "E-mail"),
            buttonType = ButtonType.Secondary,
            onClick = onNavigateToRegisterEmailPage
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
        Text(
            text = stringResource(R.string.by_registering_you_agree),
            style = Theme.typography.bodySmall,
            color = Gray50,
            textAlign = TextAlign.Center
        )
    }
}