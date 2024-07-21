package com.jefisu.auth.presentation.auth_provider_pages.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jefisu.auth.R
import com.jefisu.auth.presentation.auth_provider_pages.login.components.ForgotPasswordBottomSheet
import com.jefisu.auth.presentation.auth_provider_pages.login.components.RoundCheckbox
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.StandardButton
import com.jefisu.ui.components.StandardTextField
import com.jefisu.ui.components.loadingInButton
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Theme

@Composable
fun LoginPage(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onNavigateToRegisterPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    ForgotPasswordBottomSheet(
        state = state,
        onAction = onAction
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardTextField(
            text = state.email,
            onTextChange = { onAction(LoginAction.EmailChanged(it)) },
            fieldName = stringResource(R.string.login),
            isEnabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        StandardTextField(
            text = state.password,
            onTextChange = { onAction(LoginAction.PasswordChanged(it)) },
            fieldName = stringResource(R.string.password),
            isPasswordField = true,
            isEnabled = !state.isLoading,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraSmall))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RoundCheckbox(
                isChecked = state.rememberMeCredentials,
                onCheckedChange = {
                    onAction(LoginAction.RememberMeCredentials)
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
                onClick = { onAction(LoginAction.ToggleForgotPasswordBottomSheet) }
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = Theme.typography.bodyMedium,
                    color = Gray50
                )
            }
        }
        Spacer(modifier = Modifier.height(Theme.spacing.extraSmall))
        StandardButton(
            text = stringResource(R.string.sign_in),
            buttonType = ButtonType.Primary,
            isEnabled = !state.isLoading,
            onClick = { onAction(LoginAction.Login) },
            dynamicContent = loadingInButton(state.isLoading && !state.showForgotPasswordSheet)
        )
        Spacer(modifier = Modifier.height(152.dp))
        Text(
            text = stringResource(R.string.if_you_don_t_have_an_account_yet),
            style = Theme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(20.dp))
        StandardButton(
            text = stringResource(R.string.sign_up),
            buttonType = ButtonType.Secondary,
            onClick = onNavigateToRegisterPage
        )
    }
}