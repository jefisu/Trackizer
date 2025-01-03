package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography

@Composable
fun TrackizerAlertBottomSheet(
    sheetState: ModalBottomSheetState,
    title: String,
    description: String,
    onDismissTextButton: String,
    onConfirmTextButton: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TrackizerBottomSheet(
        sheetState = sheetState,
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_notification),
            contentDescription = null,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.small))
        Text(
            text = title,
            style = TrackizerTheme.typography.headline5,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        Text(
            text = description,
            style = TrackizerTheme.typography.bodyLarge,
            color = Gray50,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.large))
        Row {
            TrackizerButton(
                text = onDismissTextButton,
                type = ButtonType.Secondary,
                onClick = {
                    sheetState.currentDetent = SheetDetent.Hidden
                    onDismiss()
                },
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(TrackizerTheme.spacing.medium))
            TrackizerButton(
                text = onConfirmTextButton,
                type = ButtonType.Primary,
                onClick = {
                    sheetState.currentDetent = SheetDetent.Hidden
                    onConfirm()
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}