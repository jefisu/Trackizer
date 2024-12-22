package com.jefisu.auth.presentation.login.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.jefisu.auth.R
import com.jefisu.auth.presentation.login.LoginAction
import com.jefisu.auth.presentation.login.LoginEvent
import com.jefisu.auth.presentation.login.LoginState
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.ui.ObserveAsEvents
import com.jefisu.ui.UiEventController

@Composable
fun ForgotPasswordBottomSheet(
    sheetState: ModalBottomSheetState,
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    ObserveAsEvents(UiEventController.events) { event ->
        if (event is LoginEvent.HideForgotPasswordBottomSheet) {
            sheetState.currentDetent = SheetDetent.Hidden
        }
    }

    TrackizerBottomSheet(
        sheetState = sheetState,
        onDismiss = { },
    ) {
        Text(
            text = stringResource(R.string.forgot_your_password),
            style = TrackizerTheme.typography.headline5,
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
        Text(
            text = stringResource(R.string.reset_instructions_will_be_send_to_your_email),
            style = TrackizerTheme.typography.bodyMedium,
            color = Gray50,
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraMedium))
        TrackizerTextField(
            text = state.emailResetPassword,
            onTextChange = { onAction(LoginAction.EmailResetPasswordChanged(it)) },
            fieldName = stringResource(R.string.e_mail_address),
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.extraMedium))
        TrackizerButton(
            text = stringResource(R.string.send),
            onClick = { onAction(LoginAction.SendResetPassword) },
            type = ButtonType.Primary,
            isLoading = state.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}