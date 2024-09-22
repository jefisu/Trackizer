package com.jefisu.home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
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
import com.jefisu.ui.UiEventController
import com.jefisu.ui.event.NavigationEvent
import com.jefisu.ui.util.SampleData
import kotlinx.coroutines.launch

@Composable
internal fun SubscriptionList(
    subscriptions: List<Subscription>,
    messageEmptyList: String,
    modifier: Modifier = Modifier,
    upcomingBill: Boolean = false,
) {
    val lazyListState = rememberLazyListState()
    val showDivider by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0 ||
                lazyListState.isScrollInProgress &&
                lazyListState.canScrollBackward
        }
    }
    val bottomPadding = 70.dp
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = subscriptions.isEmpty(),
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = bottomPadding),
        ) {
            Text(
                text = messageEmptyList,
                color = Gray50,
                style = TrackizerTheme.typography.bodyLarge,
            )
        }

        AnimatedVisibility(
            visible = subscriptions.isNotEmpty(),
            enter = fadeIn() + slideInVertically(animationSpec = tween(700)),
            exit = fadeOut() + slideOutVertically(animationSpec = tween(700)),
        ) {
            Column {
                AnimatedVisibility(visible = showDivider) {
                    HorizontalDivider(color = Gray50)
                }
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
                    contentPadding = PaddingValues(
                        bottom = bottomPadding,
                        start = TrackizerTheme.spacing.extraMedium,
                        end = TrackizerTheme.spacing.extraMedium,
                    ),
                    state = lazyListState,
                    modifier = modifier.weight(1f),
                ) {
                    items(
                        items = subscriptions,
                        key = { it.id },
                    ) { sub ->
                        SubscriptionItem(
                            subscription = sub,
                            upcomingBill = upcomingBill,
                            modifier = Modifier.rippleClickable {
                                scope.launch {
                                    UiEventController.sendEvent(
                                        NavigationEvent.NavigateToSubscriptionInfo(sub.id),
                                    )
                                }
                            },
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier.windowInsetsPadding(
                                WindowInsets.safeContent.only(WindowInsetsSides.Bottom),
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SubscriptionItem(
    subscription: Subscription,
    modifier: Modifier = Modifier,
    upcomingBill: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Gray70,
                shape = Shapes().large,
            )
            .padding(
                top = 12.dp,
                bottom = 12.dp,
                start = 12.dp,
                end = 17.dp,
            ),
    ) {
        if (upcomingBill) {
            DateIcon(subscription.paymentDate)
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
            messageEmptyList = stringResource(R.string.you_don_t_have_any_subscriptions),
        )
    }
}
