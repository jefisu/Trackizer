package com.jefisu.auth.presentation.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.auth.R
import com.jefisu.auth.presentation.register.components.PasswordStrengthMeter
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerPasswordTextField
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.imeOffset
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.screen.LocalScreenIsSmall

@Composable
fun RegisterScreenRoot(navigateToLogin: () -> Unit) {
    val viewModel = hiltViewModel<RegisterViewModel>()
    val state = viewModel.state

    RegisterScreen(
        state = state,
        onAction = viewModel::onAction,
        navigateToLogin = navigateToLogin,
    )
}

@Composable
internal fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    navigateToLogin: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val isSmallScreen = LocalScreenIsSmall.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(TrackizerTheme.spacing.extraMedium),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .imeOffset(
                    imeThresholdPercent = if (isSmallScreen) 0.3f else 0.1f,
                ),
        ) {
            TrackizerTextField(
                text = state.email,
                onTextChange = { onAction(RegisterAction.EmailChanged(it)) },
                fieldName = stringResource(UiRes.string.email),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions {
                    focusManager.moveFocus(FocusDirection.Down)
                },
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
            TrackizerPasswordTextField(
                text = state.password,
                onTextChange = { onAction(RegisterAction.PasswordChanged(it)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraMedium))
            PasswordStrengthMeter(
                passwordStrength = state.passwordStrength,
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
            Text(
                text = stringResource(R.string.use_8_or_more_characters),
                style = TrackizerTheme.typography.bodySmall,
                color = Gray50,
                textAlign = TextAlign.Justify,
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
            TrackizerButton(
                text = stringResource(R.string.get_started_it_s_free),
                type = ButtonType.Primary,
                onClick = {
                    onAction(RegisterAction.Register)
                },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Column(
            modifier = Modifier.align(Alignment.BottomStart),
        ) {
            Text(
                text = stringResource(R.string.do_you_have_already_an_account),
                style = TrackizerTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraMedium))
            TrackizerButton(
                text = stringResource(R.string.sign_in),
                type = ButtonType.Secondary,
                onClick = navigateToLogin,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    TrackizerTheme {
        RegisterScreen(
            state = RegisterState(),
            onAction = {},
            navigateToLogin = {},
        )
    }
}