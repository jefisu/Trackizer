@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray80
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TrackizerBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
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
            content()
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
