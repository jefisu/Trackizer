package com.jefisu.add_subscription.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.SubscriptionIcon
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.domain.model.SubscriptionService

@Composable
internal fun SubscriptionServicePageItem(
    service: SubscriptionService,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        SubscriptionIcon(
            icon = service,
            containerSize = 161.dp,
            cornerSize = 50.dp,
            iconSize = TrackizerTheme.size.iconExtraLarge,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        Text(
            text = service.title,
            style = TrackizerTheme.typography.headline3,
        )
    }
}

@Preview
@Composable
private fun SubscriptionServicePageItemPreview() {
    TrackizerTheme {
        SubscriptionServicePageItem(
            service = SubscriptionService.SPOTIFY,
            modifier = Modifier
                .padding(TrackizerTheme.spacing.large),
        )
    }
}
