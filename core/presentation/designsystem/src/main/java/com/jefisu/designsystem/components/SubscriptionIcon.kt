package com.jefisu.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Microsoft365ContainerColor
import com.jefisu.designsystem.SpotifyContainerColor
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.size
import com.jefisu.designsystem.util.asIconResource
import com.jefisu.domain.model.SubscriptionService

@Composable
fun SubscriptionIcon(
    icon: SubscriptionService,
    modifier: Modifier = Modifier,
    iconSize: Dp = TrackizerTheme.size.iconDefault,
    containerSize: Dp = 40.dp,
    cornerSize: Dp = 12.dp,
) {
    val containerColor = when (icon) {
        SubscriptionService.YOUTUBE_PREMIUM -> Color.Red
        SubscriptionService.SPOTIFY -> SpotifyContainerColor
        SubscriptionService.MICROSOFT_365 -> Microsoft365ContainerColor
        SubscriptionService.NETFLIX, SubscriptionService.HBO_GO -> Color.Black
    }

    TrackizerIcon(
        containerColor = containerColor,
        containerSize = containerSize,
        cornerSize = cornerSize,
        modifier = modifier,
        content = {
            Image(
                painter = painterResource(id = icon.asIconResource()),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
            )
        },
    )
}

@Preview
@Composable
private fun SubscriptionIconPreview() {
    TrackizerTheme {
        SubscriptionIcon(SubscriptionService.SPOTIFY)
    }
}