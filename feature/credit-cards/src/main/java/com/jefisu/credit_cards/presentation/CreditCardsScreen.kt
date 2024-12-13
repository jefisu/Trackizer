@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.credit_cards.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.credit_cards.R
import com.jefisu.credit_cards.presentation.components.AddCreditCardBottomSheet
import com.jefisu.credit_cards.presentation.components.CreditCard
import com.jefisu.designsystem.Gray70
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
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.screen.LocalScreenIsSmall
import com.jefisu.ui.util.SampleData
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.ui.R as UiRes

@Composable
internal fun CreditCardsScreen(
    state: CreditCardState,
    onAction: (CreditCardAction) -> Unit,
) {
    val isSmallScreen = LocalScreenIsSmall.current
    val deleteSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)

    TrackizerAlertBottomSheet(
        sheetState = deleteSheetState,
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

    val addSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    AddCreditCardBottomSheet(
        sheetState = addSheetState,
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
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Gray70,
            ) {
                TrackizerOutlinedButton(
                    text = stringResource(R.string.add_new_card),
                    contentPadding = PaddingValues(TrackizerTheme.spacing.medium),
                    onClick = {
                        onAction(CreditCardAction.ToogleAddCreditCardBottomSheet())
                        addSheetState.currentDetent = SheetDetent.FullyExpanded
                    },
                    modifier = Modifier
                        .padding(horizontal = TrackizerTheme.spacing.extraMedium)
                        .padding(
                            bottom = TrackizerTheme.size.bottomNavigationHeight * .95f,
                            top = if (isSmallScreen) {
                                TrackizerTheme.spacing.medium
                            } else {
                                TrackizerTheme.spacing.extraMedium
                            },
                        ),
                )
            }
        },
    ) { innerPadding ->
        Crossfade(
            targetState = state.creditCards,
        ) { creditCardsMap ->
            if (creditCardsMap.isEmpty()) {
                EmptyData(
                    text = stringResource(
                        id = R.string.you_haven_t_added_any_data_yet,
                        stringResource(UiRes.string.credit_cards).lowercase(),
                    ),
                )
            } else {
                CubeOutRotationEndlessTransition(
                    items = creditCardsMap.keys.toList(),
                    onItemVisibleChanged = { onAction(CreditCardAction.SelectCreditCard(it)) },
                    modifier = Modifier.padding(
                        top = if (isSmallScreen) {
                            TrackizerTheme.spacing.extraSmall
                        } else {
                            TrackizerTheme.spacing.extraLarge
                        },
                    ),
                ) { card ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    ) {
                        CreditCard(
                            card = card,
                            onClick = {
                                onAction(CreditCardAction.ToogleAddCreditCardBottomSheet(card))
                                addSheetState.currentDetent = SheetDetent.FullyExpanded
                            },
                            onLongClick = {
                                onAction(CreditCardAction.ToogleDeleteAlert(card))
                                deleteSheetState.currentDetent = SheetDetent.FullyExpanded
                            },
                        )
                        val subscriptionServices = creditCardsMap[card].orEmpty()
                        if (subscriptionServices.isEmpty()) {
                            EmptyData(
                                text = stringResource(
                                    id = R.string.you_haven_t_added_any_data_yet,
                                    stringResource(UiRes.string.subscription).lowercase(),
                                ),
                                showImage = false,
                            )
                        } else {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        top = TrackizerTheme.spacing.extraSmall,
                                    ),
                            ) {
                                Text(
                                    text = stringResource(UiRes.string.subscriptions),
                                    style = TrackizerTheme.typography.headline3,
                                )
                                Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
                                SubscriptionIconsRow(subscriptionServices = subscriptionServices)
                            }
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
    limitVisible: Int = 4,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
        modifier = Modifier.height(TrackizerTheme.size.iconMedium),
    ) {
        subscriptionServices.take(limitVisible).forEach { SubscriptionIcon(icon = it) }

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
        modifier = Modifier.fillMaxSize(),
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
            modifier = Modifier.padding(horizontal = TrackizerTheme.spacing.extraMedium),
        )
    }
}

private class CreditCardStatePreviewParameter : PreviewParameterProvider<CreditCardState> {
    val state = CreditCardState(
        creditCards = SampleData.cards,
        selectedCard = SampleData.cards.keys.first(),
    )

    override val values: Sequence<CreditCardState> = sequenceOf(
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
    @PreviewParameter(CreditCardStatePreviewParameter::class) state: CreditCardState,
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
