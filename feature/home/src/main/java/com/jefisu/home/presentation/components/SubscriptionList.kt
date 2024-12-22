package com.jefisu.home.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.SubscriptionIcon
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.formatCurrency
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.domain.model.Subscription
import com.jefisu.home.R
import com.jefisu.ui.util.SampleData

@Composable
internal fun SubscriptionList(
    subscriptions: List<Subscription>,
    messageEmptyList: String,
    modifier: Modifier = Modifier,
    upcomingBill: Boolean = false,
    onItemClick: (Subscription) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()
    val showDivider by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0 || lazyListState.firstVisibleItemScrollOffset > 0
        }
    }
    val bottomPadding = 70.dp

    AnimatedContent(
        targetState = subscriptions,
        contentAlignment = Alignment.Center,
        transitionSpec = {
            val animationSpec = tween<IntOffset>(durationMillis = 700)
            fadeIn() + slideInVertically(
                animationSpec = animationSpec,
            ) togetherWith fadeOut() + slideOutVertically(
                animationSpec = animationSpec,
            )
        },
        modifier = modifier
            .windowInsetsPadding(WindowInsets.safeContent.only(WindowInsetsSides.Bottom)),
    ) { subs ->
        if (subs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = messageEmptyList,
                    color = Gray50,
                    style = TrackizerTheme.typography.bodyLarge,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = bottomPadding),
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
                contentPadding = PaddingValues(
                    bottom = bottomPadding,
                    start = TrackizerTheme.spacing.extraMedium,
                    end = TrackizerTheme.spacing.extraMedium,
                ),
                state = lazyListState,
                modifier = Modifier
                    .drawWithContent {
                        drawContent()
                        if (showDivider) {
                            drawLine(
                                color = Gray50,
                                start = Offset(0f, DividerDefaults.Thickness.toPx()),
                                end = Offset(size.width, DividerDefaults.Thickness.toPx()),
                                strokeWidth = DividerDefaults.Thickness.toPx(),
                            )
                        }
                    },
            ) {
                items(
                    items = subscriptions,
                    key = { it.id },
                ) { sub ->
                    SubscriptionItem(
                        subscription = sub,
                        upcomingBill = upcomingBill,
                        onClick = { onItemClick(sub) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SubscriptionItem(
    subscription: Subscription,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    upcomingBill: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(Shapes().large)
            .border(
                width = 1.dp,
                color = Gray70,
                shape = Shapes().large,
            )
            .rippleClickable {
                onClick()
            }
            .padding(
                top = 12.dp,
                bottom = 12.dp,
                start = 12.dp,
                end = 17.dp,
            ),
    ) {
        if (upcomingBill) {
            DateIcon(subscription.firstPayment)
        } else {
            SubscriptionIcon(subscription.service)
        }
        Text(
            text = subscription.service.title,
            style = TrackizerTheme.typography.headline2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = formatCurrency(
                value = subscription.price.toDouble(),
                isCompactFormat = false,
            ),
            style = TrackizerTheme.typography.headline2,
        )
    }
}

private class SubscriptionListPreviewParameter : PreviewParameterProvider<List<Subscription>> {
    override val values = sequenceOf(
        SampleData.subscriptions,
        emptyList(),
    )
}

@Preview
@Composable
private fun SubscriptionListPreview(
    @PreviewParameter(SubscriptionListPreviewParameter::class) subscriptions: List<Subscription>,
) {
    TrackizerTheme {
        SubscriptionList(
            subscriptions = subscriptions,
            messageEmptyList = stringResource(R.string.you_don_t_have_any),
        )
    }
}