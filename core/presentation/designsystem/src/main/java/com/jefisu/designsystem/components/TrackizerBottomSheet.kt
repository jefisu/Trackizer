@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TrackizerBottomSheet(
    modifier: Modifier = Modifier,
    horizontalAligment: Alignment.Horizontal = Alignment.CenterHorizontally,
    isVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            properties = ModalBottomSheetDefaults.properties(
                shouldDismissOnBackPress = false,
            ),
            containerColor = Gray80,
            windowInsets = WindowInsets.ime,
            modifier = modifier,
            dragHandle = {
                BottomSheetDefaults.DragHandle(color = Gray50)
            },
        ) {
            Column(
                horizontalAlignment = horizontalAligment,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = TrackizerTheme.spacing.extraMedium,
                        start = TrackizerTheme.spacing.extraMedium,
                        end = TrackizerTheme.spacing.extraMedium,
                    )
                    .navigationBarsPadding(),
            ) {
                content()
            }
        }
    }
}

fun SheetState.hideSheet(
    scope: CoroutineScope,
    onDismiss: () -> Unit,
) {
    scope.launch { hide() }.invokeOnCompletion {
        if (!isVisible) {
            onDismiss()
        }
    }
}
