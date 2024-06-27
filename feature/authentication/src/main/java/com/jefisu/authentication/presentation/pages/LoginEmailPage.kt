package com.jefisu.authentication.presentation.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jefisu.authentication.R
import com.jefisu.authentication.presentation.AuthAction
import com.jefisu.authentication.presentation.AuthState
import com.jefisu.authentication.presentation.components.RoundCheckbox
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.DynamicButton
import com.jefisu.ui.components.StandardTextField
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Theme

@Composable
fun LoginEmailPage(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    onNavigateToRegisterPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardTextField(
            text = state.loginEmail,
            onTextChange = { onAction(AuthAction.LoginEmailChanged(it)) },
            fieldName = stringResource(R.string.login)
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        StandardTextField(
            text = state.loginPassword,
            onTextChange = { onAction(AuthAction.LoginPasswordChanged(it)) },
            fieldName = stringResource(R.string.password),
            isPasswordField = true
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraSmall))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundCheckbox(
                isChecked = state.rememberMeCredentials,
                onCheckedChange = {
                    onAction(AuthAction.RemeberMeCredentials)
                }
            )
            Spacer(modifier = Modifier.width(Theme.spacing.small))
            Text(
                text = stringResource(R.string.remember_me),
                style = Theme.typography.bodyMedium,
                color = Gray50
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { onAction(AuthAction.LoginForgotPassword) }
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = Theme.typography.bodyMedium,
                    color = Gray50
                )
            }
        }
        Spacer(modifier = Modifier.height(Theme.spacing.extraSmall))
        DynamicButton(
            text = stringResource(R.string.sign_in),
            buttonType = ButtonType.Primary,
            onClick = { onAction(AuthAction.SubmitLogin) }
        )
        Spacer(modifier = Modifier.height(152.dp))
        Text(
            text = stringResource(R.string.if_you_don_t_have_an_account_yet),
            style = Theme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        DynamicButton(
            text = stringResource(R.string.sign_up),
            buttonType = ButtonType.Secondary,
            onClick = onNavigateToRegisterPage
        )
    }
}