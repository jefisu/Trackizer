@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.ui.R as UiRes

@Composable
fun <T> TrackizerOptionPicker(
    title: String,
    visible: Boolean,
    items: List<T>,
    onSelectClick: (T) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    startIndex: Int = 0,
    itemContent: @Composable (T) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val pickerState = rememberTrackizerPickerState(
        itemsCount = items.size,
        startIndex = startIndex,
    )
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(visible) {
        if (!visible && pickerState.selectedIndex != startIndex) {
            pickerState.setIndex(startIndex)
        }
    }

    TrackizerBottomSheet(
        isVisible = visible,
        sheetState = sheetState,
        horizontalAligment = Alignment.Start,
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = TrackizerTheme.typography.headline4,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.small))
        TrackizerPicker(
            state = pickerState,
            items = items,
            itemContent = itemContent,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraSmall))
        TrackizerButton(
            text = stringResource(UiRes.string.select),
            type = ButtonType.Primary,
            onClick = {
                onSelectClick(items[pickerState.selectedIndex])
                sheetState.hideSheet(
                    scope = scope,
                    onDismiss = onDismiss,
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
