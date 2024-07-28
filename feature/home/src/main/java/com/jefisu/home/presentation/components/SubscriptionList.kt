package com.jefisu.home.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.domain.model.Subscription
import com.jefisu.home.presentation.util.SampleData
import com.jefisu.home.presentation.util.formatCurrency
import com.jefisu.ui.components.IconSize
import com.jefisu.ui.components.SubscriptionIcon
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Gray70
import com.jefisu.ui.theme.Theme

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
    val bottomPadding = 80.dp

    if (subscriptions.isEmpty()) {
        Text(
            text = messageEmptyList,
            color = Gray50,
            style = Theme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = bottomPadding)
        )
    } else {
        Column {
            AnimatedVisibility(visible = showDivider) {
                HorizontalDivider(color = Gray50)
            }
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.small),
                contentPadding = PaddingValues(
                    bottom = bottomPadding,
                    start = Theme.spacing.extraMedium,
                    end = Theme.spacing.extraMedium,
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
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SubscriptionListPreview() {
    AppTheme {
        SubscriptionList(
            subscriptions = SampleData.subscriptions,
            messageEmptyList = "No subscriptions found",
        )
    }
}

@Composable
private fun SubscriptionItem(
    subscription: Subscription,
    modifier: Modifier = Modifier,
    upcomingBill: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Gray70,
                shape = RoundedCornerShape(16.dp),
            )
            .padding(
                top = 12.dp,
                bottom = 12.dp,
                start = 12.dp,
                end = 17.dp,
            ),
    ) {
        if (upcomingBill) {
            DateIcon(subscription.firstPaymentDate)
        } else {
            SubscriptionIcon(
                icon = subscription.icon,
                size = IconSize.SMALL,
            )
        }
        Text(
            text = subscription.name,
            style = Theme.typography.headline2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = formatCurrency(subscription.price.toDouble()),
            style = Theme.typography.headline2,
        )
    }
}
