package com.jefisu.auth.presentation.auth_provider_pages.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jefisu.auth.R
import com.jefisu.auth.presentation.auth_provider_pages.register.components.PasswordStrengthMeter
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.StandardButton
import com.jefisu.ui.components.StandardTextField
import com.jefisu.ui.components.loadingInButton
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Theme

@Composable
fun RegisterPage(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    onNavigateToLoginPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardTextField(
            text = state.email,
            onTextChange = { onAction(RegisterAction.EmailChanged(it)) },
            fieldName = stringResource(R.string.e_mail_address),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        StandardTextField(
            text = state.password,
            onTextChange = { onAction(RegisterAction.PasswordChanged(it)) },
            fieldName = stringResource(R.string.password),
            isPasswordField = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            }
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
        PasswordStrengthMeter(
            passwordStrength = state.passwordStrength
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        Text(
            text = stringResource(R.string.use_8_or_more_characters),
            style = Theme.typography.bodySmall,
            color = Gray50,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(40.dp))
        StandardButton(
            text = stringResource(R.string.get_started_it_s_free),
            buttonType = ButtonType.Primary,
            onClick = {
                onAction(RegisterAction.Register)
            },
            dynamicContent = loadingInButton(state.isLoading)
        )
        Spacer(modifier = Modifier.height(130.dp))
        Text(
            text = stringResource(R.string.do_you_have_already_an_account),
            style = Theme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
        StandardButton(
            text = stringResource(R.string.sign_in),
            buttonType = ButtonType.Secondary,
            onClick = onNavigateToLoginPage
        )
    }
}