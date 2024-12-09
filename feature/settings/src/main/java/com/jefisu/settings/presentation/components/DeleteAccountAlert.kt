package com.jefisu.settings.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerPasswordTextField
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.settings.presentation.SettingsAction
import com.jefisu.settings.presentation.SettingsEvent
import com.jefisu.settings.presentation.SettingsState
import com.jefisu.ui.ObserveAsEvents
import com.jefisu.ui.UiEvent
import com.jefisu.ui.UiEventController
import com.jefisu.ui.R as UiRes

@Composable
fun DeleteAccountAlert(
    sheetState: ModalBottomSheetState,
    settingsState: SettingsState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ObserveAsEvents(UiEventController.events) { event ->
        if (event is SettingsEvent.DeletedAccount) {
            sheetState.currentDetent = SheetDetent.Hidden
        }
    }

    TrackizerBottomSheet(
        sheetState = sheetState,
        onDismiss = {},
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_notification),
            contentDescription = null,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.small))
        Text(
            text = stringResource(com.jefisu.settings.R.string.delete_account),
            style = TrackizerTheme.typography.headline5,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        Text(
            text = stringResource(com.jefisu.settings.R.string.delete_account_description),
            style = TrackizerTheme.typography.bodyLarge,
            color = Gray50,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.medium))
        TrackizerPasswordTextField(
            text = settingsState.userPassword,
            onTextChange = { onAction(SettingsAction.PasswordChanged(it)) },
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.large))
        Row {
            TrackizerButton(
                text = stringResource(UiRes.string.button_alert_cancel),
                type = ButtonType.Secondary,
                onClick = {
                    sheetState.currentDetent = SheetDetent.Hidden
                    onAction(SettingsAction.PasswordChanged(""))
                },
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(TrackizerTheme.spacing.medium))
            TrackizerButton(
                text = stringResource(UiRes.string.button_alert_delete),
                type = ButtonType.Primary,
                enabled = settingsState.canDeleteAccount,
                isLoading = settingsState.deleteAccountInProgress,
                onClick = {
                    onAction(SettingsAction.DeleteAccount)
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}