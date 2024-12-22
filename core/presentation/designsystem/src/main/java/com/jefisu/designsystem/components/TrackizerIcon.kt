package com.jefisu.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing

@Composable
fun TrackizerIcon(
    modifier: Modifier = Modifier,
    containerColor: Color = Gray70,
    contentColor: Color = Gray30,
    containerSize: Dp = TrackizerTheme.size.iconMedium,
    cornerSize: Dp = 12.dp,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalContentColor provides contentColor,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(containerSize)
                .clip(RoundedCornerShape(cornerSize))
                .background(containerColor),
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun SubscriptionIconPreview() {
    TrackizerTheme {
        TrackizerIcon(
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
            )
        }
    }
}