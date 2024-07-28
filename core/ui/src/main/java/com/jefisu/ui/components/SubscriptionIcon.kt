package com.jefisu.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.domain.model.ServiceIcon
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Microsoft365ContainerColor
import com.jefisu.ui.theme.SpotifyContainerColor
import com.jefisu.ui.theme.Theme
import com.jefisu.ui.util.asIconResource

@Composable
fun SubscriptionIcon(
    icon: ServiceIcon,
    size: IconSize,
    modifier: Modifier = Modifier,
) {
    val iconSize = when (size) {
        IconSize.SMALL -> 24.dp
    }
    val containerSize = when (size) {
        IconSize.SMALL -> 40.dp
    }
    val cornerSize = when (size) {
        IconSize.SMALL -> 12.dp
    }
    val containerColor = when (icon) {
        ServiceIcon.YOUTUBE_PREMIUM -> Color.Red
        ServiceIcon.SPOTIFY -> SpotifyContainerColor
        ServiceIcon.MICROSOFT_365 -> Microsoft365ContainerColor
        ServiceIcon.NETFLIX, ServiceIcon.HBO_GO -> Color.Black
    }

    Icon(
        containerColor = containerColor,
        containerSize = containerSize,
        cornerSize = cornerSize,
        modifier = modifier,
        content = {
            Image(
                painter = painterResource(id = icon.asIconResource()),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.width(iconSize),
            )
        },
    )
}

@Composable
fun Icon(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    containerSize: Dp = 40.dp,
    cornerSize: Dp = 12.dp,
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

enum class IconSize {
    SMALL,
}

@Preview
@Composable
private fun SubscriptionIconPreview() {
    AppTheme {
        SubscriptionIcon(
            icon = ServiceIcon.MICROSOFT_365,
            size = IconSize.SMALL,
            modifier = Modifier.padding(Theme.spacing.small),
        )
    }
}
