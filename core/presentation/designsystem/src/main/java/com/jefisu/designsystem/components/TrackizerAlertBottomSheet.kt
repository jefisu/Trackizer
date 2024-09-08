@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography

@Composable
fun TrackizerAlertBottomSheet(
    isVisible: Boolean,
    title: String,
    description: String,
    onDismissTextButton: String,
    onConfirmTextButton: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    TrackizerBottomSheet(
        isVisible = isVisible,
        sheetState = state,
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = TrackizerTheme.spacing.extraMedium)
                .padding(bottom = TrackizerTheme.spacing.extraMedium),
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
                        state.hideSheet(scope, onDismiss)
                    },
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(TrackizerTheme.spacing.medium))
                TrackizerButton(
                    text = onConfirmTextButton,
                    type = ButtonType.Primary,
                    onClick = {
                        state.hideSheet(scope, onConfirm)
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
