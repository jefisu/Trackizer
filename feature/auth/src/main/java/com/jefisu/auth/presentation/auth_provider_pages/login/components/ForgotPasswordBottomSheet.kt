package com.jefisu.auth.presentation.auth_provider_pages.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jefisu.auth.R
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginAction
import com.jefisu.auth.presentation.auth_provider_pages.login.LoginState
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.StandardButton
import com.jefisu.ui.components.StandardTextField
import com.jefisu.ui.components.loadingInButton
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordBottomSheet(
    modifier: Modifier = Modifier,
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    if (state.showForgotPasswordSheet) {
        ModalBottomSheet(
            onDismissRequest = { onAction(LoginAction.ToggleForgotPasswordBottomSheet) },
            sheetState = sheetState,
            modifier = modifier,
            dragHandle = {},
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false
            ),
            containerColor = Theme.backgroundColor,
            windowInsets = WindowInsets.ime
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        start = Theme.spacing.extraMedium,
                        end = Theme.spacing.extraMedium,
                        top = Theme.spacing.extraSmall,
                        bottom = Theme.spacing.extraMedium
                    )
            ) {
                IconButton(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) onAction(LoginAction.ToggleForgotPasswordBottomSheet)
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(R.string.forgot_your_password),
                    style = Theme.typography.headline5
                )
                Spacer(modifier = Modifier.height(Theme.spacing.medium))
                Text(
                    text = stringResource(R.string.reset_instructions_will_be_send_to_your_email),
                    style = Theme.typography.bodyMedium,
                    color = Gray50
                )
                Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
                StandardTextField(
                    text = state.emailResetPassword,
                    onTextChange = { onAction(LoginAction.EmailResetPasswordChanged(it)) },
                    fieldName = stringResource(R.string.e_mail_address)
                )
                Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
                StandardButton(
                    text = stringResource(R.string.send),
                    buttonType = ButtonType.Primary,
                    onClick = { onAction(LoginAction.SendResetPassword) },
                    dynamicContent = loadingInButton(state.isLoading)
                )
            }
        }
    }
}