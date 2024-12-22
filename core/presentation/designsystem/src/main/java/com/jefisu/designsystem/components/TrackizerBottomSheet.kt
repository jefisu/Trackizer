@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.designsystem.components

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing

@Composable
fun TrackizerBottomSheet(
    modifier: Modifier = Modifier,
    horizontalAligment: Alignment.Horizontal = Alignment.CenterHorizontally,
    sheetState: ModalBottomSheetState,
    onDismiss: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        state = sheetState,
        onDismiss = onDismiss,
    ) {
        Scrim(
            enter = fadeIn(),
            exit = fadeOut(),
        )
        Sheet(
            modifier = modifier
                .background(Gray80, RoundedCornerShape(32.dp, 32.dp))
                .imePadding(),
        ) {
            Column(
                horizontalAlignment = horizontalAligment,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = TrackizerTheme.spacing.small,
                        start = TrackizerTheme.spacing.extraMedium,
                        end = TrackizerTheme.spacing.extraMedium,
                    )
                    .navigationBarsPadding(),
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = TrackizerTheme.spacing.medium)
                        .height(5.dp)
                        .width(40.dp)
                        .background(Gray50, CircleShape)
                        .align(Alignment.CenterHorizontally),
                )
                content()
            }
        }
    }
}