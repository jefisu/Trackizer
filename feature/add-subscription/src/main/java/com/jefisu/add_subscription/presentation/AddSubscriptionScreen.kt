@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.add_subscription.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.add_subscription.R
import com.jefisu.add_subscription.presentation.components.SubscriptionServicePageItem
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.CurrencyTextField
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerScaffold
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.getEndlessItem
import com.jefisu.designsystem.util.imeOffset
import com.jefisu.designsystem.util.rememberEndlessPagerState
import com.jefisu.domain.model.SubscriptionService
import com.jefisu.ui.UiEventController
import com.jefisu.ui.event.NavigationEvent
import kotlinx.coroutines.launch

@Composable
internal fun AddSubscriptionScreen(
    state: AddSubscriptionState,
    onAction: (AddSubscriptionAction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val services = SubscriptionService.entries
    val pagerState = rememberEndlessPagerState(startPage = Int.MAX_VALUE / 2)
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            val item = services.getEndlessItem(index)
            onAction(AddSubscriptionAction.SubscriptionServiceChanged(item))
        }
    }

    TrackizerScaffold(
        topBar = {
            TrackizerTopBar(
                title = stringResource(R.string.new_title),
                navigationIcon = {
                    TrackizerTopBarDefaults.backNavigationIcon(
                        onClick = {
                            scope.launch { UiEventController.sendEvent(NavigationEvent.NavigateUp) }
                        },
                    )
                },
                colors = TrackizerTopBarDefaults.colors.copy(
                    containerColor = Gray70,
                ),
            )
        },
        modifier = Modifier.imeOffset(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.58f)
                    .clip(RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp))
                    .background(Gray70),
            ) {
                Text(
                    text = stringResource(R.string.add_new_subscription),
                    style = TrackizerTheme.typography.headline7,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = TrackizerTheme.spacing.large)
                        .padding(horizontal = TrackizerTheme.spacing.extraMedium),
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.extraLarge))
                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(
                        horizontal = with(TrackizerTheme.spacing) { extraLarge + small },
                    ),
                ) { page ->
                    val service = services.getEndlessItem(page)
                    SubscriptionServicePageItem(
                        service = service,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .padding(TrackizerTheme.spacing.extraMedium),
            ) {
                TrackizerTextField(
                    text = state.description,
                    onTextChange = { onAction(AddSubscriptionAction.DescriptionChanged(it)) },
                    fieldName = stringResource(R.string.description),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                    ),
                    keyboardActions = KeyboardActions {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                )
                CurrencyTextField(
                    text = state.price,
                    onTextChange = { onAction(AddSubscriptionAction.PriceChanged(it)) },
                    label = stringResource(R.string.monthly_price),
                    keyboardActions = KeyboardActions {
                        focusManager.clearFocus()
                    },
                )
                TrackizerButton(
                    text = stringResource(R.string.add_this_platform),
                    type = ButtonType.Primary,
                    onClick = { onAction(AddSubscriptionAction.AddSubscription) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddSubscriptionScreenPreview() {
    TrackizerTheme {
        AddSubscriptionScreen(
            state = AddSubscriptionState(),
            onAction = {},
        )
    }
}