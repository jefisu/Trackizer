package com.jefisu.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.auth.R
import com.jefisu.auth.presentation.login.components.ForgotPasswordBottomSheet
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.LabeledCheckbox
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerPasswordTextField
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.imeOffset

@Composable
fun LoginScreenRoot(
    navigateToRegister: () -> Unit,
) {
    val viewModel = hiltViewModel<LoginViewModel>()
    val state = viewModel.state

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        navigateToRegisterScreen = navigateToRegister,
    )
}

@Composable
internal fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    navigateToRegisterScreen: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val forgotPasswordSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    ForgotPasswordBottomSheet(
        sheetState = forgotPasswordSheetState,
        state = state,
        onAction = onAction,
    )

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(TrackizerTheme.spacing.extraMedium)
            .imeOffset(imeThresholdPercent = 0.25f),
    ) {
        TrackizerTextField(
            text = state.email,
            onTextChange = { onAction(LoginAction.EmailChanged(it)) },
            fieldName = stringResource(R.string.login),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions {
                focusManager.moveFocus(FocusDirection.Down)
            },
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
        TrackizerPasswordTextField(
            text = state.password,
            onTextChange = { onAction(LoginAction.PasswordChanged(it)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
            },
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraSmall))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LabeledCheckbox(
                isChecked = state.rememberMeCredentials,
                onCheckedChange = { onAction(LoginAction.RememberMeCredentials) },
                label = stringResource(R.string.remember_me),
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                onClick = { forgotPasswordSheetState.currentDetent = SheetDetent.FullyExpanded },
            ) {
                Text(
                    text = stringResource(R.string.forgot_password),
                    style = TrackizerTheme.typography.bodyMedium,
                    color = Gray50,
                )
            }
        }
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraSmall))
        TrackizerButton(
            text = stringResource(R.string.sign_in),
            type = ButtonType.Primary,
            isLoading = state.isLoading && forgotPasswordSheetState.currentDetent == SheetDetent.Hidden,
            onClick = { onAction(LoginAction.Login) },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(152.dp))
        Text(
            text = stringResource(R.string.if_you_don_t_have_an_account_yet),
            style = TrackizerTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(20.dp))
        TrackizerButton(
            text = stringResource(R.string.sign_up),
            type = ButtonType.Secondary,
            onClick = navigateToRegisterScreen,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    TrackizerTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {},
            navigateToRegisterScreen = {},
        )
    }
}
