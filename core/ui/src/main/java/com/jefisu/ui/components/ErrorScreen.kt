package com.jefisu.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Theme

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    error: String?,
    onErrorDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    var isVisible by rememberSaveable(error) {
        mutableStateOf(error != null)
    }
    val scrimColor by animateColorAsState(
        targetValue = run {
            if (isVisible) BottomSheetDefaults.ScrimColor
            else Color.Transparent
        },
        label = ""
    )

    Box(modifier = modifier) {
        content()
        DisableUserInteraction(
            disabled = isVisible
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scrimColor)
                .systemBarsPadding()
        ) {
            AnimatedVisibility(
                visible = isVisible,
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                FlashMessage(
                    message = error.orEmpty(),
                    type = FlashMessageType.ERROR,
                    onCloseClick = {
                        isVisible = false
                        onErrorDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Theme.spacing.medium)
                )
            }
        }
    }
}

@Composable
fun DisableUserInteraction(
    disabled: Boolean
) {
    if (disabled) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {}
        )
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    AppTheme {
        ErrorScreen(
            error = "Change a few things up and try submitting again.",
            onErrorDismiss = {},
            content = { }
        )
    }
}