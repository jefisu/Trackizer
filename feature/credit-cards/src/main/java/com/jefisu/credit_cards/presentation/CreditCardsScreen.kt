@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.credit_cards.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jefisu.credit_cards.R
import com.jefisu.credit_cards.presentation.components.AddCreditCardBottomSheet
import com.jefisu.credit_cards.presentation.components.CreditCard
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.AnimatedText
import com.jefisu.designsystem.components.CubeOutRotationEndlessTransition
import com.jefisu.designsystem.components.SubscriptionIcon
import com.jefisu.designsystem.components.TrackizerAlertBottomSheet
import com.jefisu.designsystem.components.TrackizerBottomNavigation
import com.jefisu.designsystem.components.TrackizerIcon
import com.jefisu.designsystem.components.TrackizerOutlinedButton
import com.jefisu.designsystem.components.TrackizerScaffold
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.domain.model.SubscriptionService
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.util.SampleData

@Composable
internal fun CreditCardsScreen(
    state: CreditCardState,
    onAction: (CreditCardAction) -> Unit,
) {
    val isEmptyCards = state.creditCards.keys.isEmpty()
    val isEmptySubscriptions = with(state) { creditCards[selectedCard]?.isEmpty() == true }

    TrackizerAlertBottomSheet(
        isVisible = state.showDeleteAlert,
        title = stringResource(
            id = UiRes.string.delete_alert_title,
            stringResource(UiRes.string.credit_card).lowercase(),
        ),
        description = stringResource(
            id = UiRes.string.delete_alert_description,
            stringResource(UiRes.string.credit_card).lowercase(),
        ),
        onDismissTextButton = stringResource(UiRes.string.button_alert_cancel),
        onConfirmTextButton = stringResource(com.jefisu.ui.R.string.button_alert_delete),
        onDismiss = {
            onAction(CreditCardAction.ToogleDeleteAlert())
        },
        onConfirm = {
            onAction(CreditCardAction.DeleteCard)
        },
    )

    AddCreditCardBottomSheet(
        state = state,
        onAction = onAction,
    )

    TrackizerScaffold(
        topBar = {
            TrackizerTopBar(
                title = stringResource(UiRes.string.credit_cards),
                actions = {
                    TrackizerTopBarDefaults.settingsActionIcon(
                        onClick = {
                            onAction(CreditCardAction.Navigate(Destination.SettingsScreen))
                        },
                    )
                },
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .height(210.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Gray70)
                    .padding(TrackizerTheme.spacing.extraMedium),
            ) {
                TrackizerOutlinedButton(
                    text = stringResource(R.string.add_new_card),
                    contentPadding = PaddingValues(TrackizerTheme.spacing.medium),
                    onClick = { onAction(CreditCardAction.ToogleAddCreditCardBottomSheet()) },
                )
            }
        },
    ) { innerPadding ->
        if (isEmptyCards) {
            EmptyData(
                text = stringResource(
                    id = R.string.you_haven_t_added_any_data_yet,
                    stringResource(UiRes.string.credit_cards).lowercase(),
                ),
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                CubeOutRotationEndlessTransition(
                    items = state.creditCards.keys.toList(),
                    onItemVisibleChanged = { onAction(CreditCardAction.SelectCreditCard(it)) },
                    content = {
                        CreditCard(
                            card = it,
                            onClick = {
                                onAction(CreditCardAction.ToogleAddCreditCardBottomSheet(it))
                            },
                            onLongClick = {
                                onAction(CreditCardAction.ToogleDeleteAlert(it))
                            },
                        )
                    },
                    modifier = Modifier.padding(top = TrackizerTheme.spacing.extraLarge),
                )
                if (isEmptySubscriptions) {
                    EmptyData(
                        text = stringResource(
                            id = R.string.you_haven_t_added_any_data_yet,
                            stringResource(UiRes.string.subscription).lowercase(),
                        ),
                        showImage = false,
                    )
                } else {
                    Spacer(Modifier.height(TrackizerTheme.spacing.extraLarge))
                    Text(
                        text = stringResource(UiRes.string.subscriptions),
                        style = TrackizerTheme.typography.headline3,
                    )
                    Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
                    Box {
                        state.creditCards.forEach { (card, subscriptionServices) ->
                            SubscriptionIconsRow(
                                subscriptionServices = subscriptionServices,
                                isVisible = card == state.selectedCard,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SubscriptionIconsRow(
    subscriptionServices: List<SubscriptionService>,
    isVisible: Boolean,
    limitVisible: Int = 4,
) {
    val alphaAnim by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        label = "alpha",
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
        modifier = Modifier
            .height(TrackizerTheme.size.iconMedium)
            .graphicsLayer {
                alpha = alphaAnim
            },
    ) {
        subscriptionServices
            .take(limitVisible)
            .forEach { SubscriptionIcon(icon = it) }

        if (subscriptionServices.size > limitVisible) {
            TrackizerIcon {
                AnimatedText(
                    text = "+${subscriptionServices.size - limitVisible}",
                    style = TrackizerTheme.typography.headline3,
                )
            }
        }
    }
}

@Composable
fun EmptyData(
    text: String,
    showImage: Boolean = true,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize(),
    ) {
        if (showImage) {
            Image(
                painter = painterResource(id = DesignSystemRes.drawable.undraw_empty),
                contentDescription = null,
                modifier = Modifier.width(180.dp),
            )
            Spacer(Modifier.height(TrackizerTheme.spacing.large))
        }
        Text(
            text = text,
            style = TrackizerTheme.typography.headline2,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = TrackizerTheme.spacing.extraMedium),
        )
    }
}

private class CreditCardStatePreviewParamater : PreviewParameterProvider<CreditCardState> {
    val state = CreditCardState(
        creditCards = SampleData.cards,
        selectedCard = SampleData.cards.keys.first(),
    )

    override val values: Sequence<CreditCardState> =
        sequenceOf(
            CreditCardState(),
            state,
            state.copy(
                selectedCard = state.creditCards.keys.last(),
            ),
        )
}

@Preview
@Composable
private fun CreditCardsScreenPreview(
    @PreviewParameter(CreditCardStatePreviewParamater::class) state: CreditCardState,
) {
    TrackizerTheme {
        TrackizerBottomNavigation {
            CreditCardsScreen(
                state = state,
                onAction = { },
            )
        }
    }
}
