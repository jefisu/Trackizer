@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.credit_cards.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jefisu.credit_cards.R
import com.jefisu.credit_cards.domain.CardConstants
import com.jefisu.credit_cards.presentation.CreditCardAction
import com.jefisu.credit_cards.presentation.CreditCardState
import com.jefisu.credit_cards.presentation.CreditCardUiEvent
import com.jefisu.credit_cards.presentation.util.asFlagResource
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.components.hideSheet
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.ui.ObserveAsEvents
import com.jefisu.ui.UiEventController
import com.steliospapamichail.creditcardmasker.viewtransformations.CardNumberMask
import com.steliospapamichail.creditcardmasker.viewtransformations.ExpirationDateMask

@Composable
fun AddCreditCardBottomSheet(
    state: CreditCardState,
    onAction: (CreditCardAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val title = stringResource(
        if (state.editCard != null) R.string.edit_card else R.string.add_card,
    )
    val buttonText = stringResource(
        if (state.editCard != null) R.string.save_changes else R.string.add_card,
    )

    ObserveAsEvents(UiEventController.events) { event ->
        if (event is CreditCardUiEvent.HideBottomSheet) {
            sheetState.hideSheet(
                scope = scope,
                onDismiss = {
                    onAction(CreditCardAction.ToogleAddCreditCardBottomSheet())
                },
            )
        }
    }

    TrackizerBottomSheet(
        isVisible = state.showAddCreditCardBottomSheet,
        sheetState = sheetState,
        onDismiss = { onAction(CreditCardAction.ToogleAddCreditCardBottomSheet()) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = TrackizerTheme.spacing.extraMedium)
                .padding(bottom = TrackizerTheme.spacing.extraMedium),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = title,
                    style = TrackizerTheme.typography.headline4,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(TrackizerTheme.spacing.medium))
                IconButton(
                    onClick = {
                        sheetState.hideSheet(
                            scope = scope,
                            onDismiss = {
                                onAction(CreditCardAction.ToogleAddCreditCardBottomSheet())
                            },
                        )
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close add card bottom sheet",
                    )
                }
            }
            Spacer(Modifier.height(TrackizerTheme.spacing.small))
            TrackizerTextField(
                text = state.cardName,
                onTextChange = {
                    if (it.length <= CardConstants.CARD_NAME_LENGTH) {
                        onAction(CreditCardAction.CardNameChanged(it))
                    }
                },
                fieldName = stringResource(R.string.card_name),
            )
            Spacer(Modifier.height(TrackizerTheme.spacing.medium))
            TrackizerTextField(
                text = state.cardHolder,
                onTextChange = {
                    if (it.length <= CardConstants.CARD_HOLDER_LENGTH) {
                        onAction(CreditCardAction.CardHolderChanged(it))
                    }
                },
                fieldName = stringResource(R.string.card_holder),
            )
            Spacer(Modifier.height(TrackizerTheme.spacing.medium))
            Row {
                TrackizerTextField(
                    text = state.cardNumber,
                    onTextChange = {
                        if (it.length <= CardConstants.CARD_NUMBER_LENGTH) {
                            onAction(CreditCardAction.CardNumberChanged(it))
                        }
                    },
                    fieldName = stringResource(R.string.card_number),
                    visualTransformation = CardNumberMask(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(TrackizerTheme.spacing.medium))
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(TrackizerTheme.size.textFieldHeight)
                        .align(Alignment.Bottom)
                        .clip(Shapes().large)
                        .border(
                            width = 1.dp,
                            color = Gray50,
                            shape = Shapes().large,
                        )
                        .scale(0.6f)
                        .paint(
                            painter = painterResource(state.flag.asFlagResource()),
                            contentScale = ContentScale.FillWidth,
                        ),
                )
            }
            Spacer(Modifier.height(TrackizerTheme.spacing.medium))
            Row {
                TrackizerTextField(
                    text = state.expirationDate,
                    onTextChange = {
                        if (it.length <= CardConstants.EXPIRATION_DATE_LENGTH) {
                            onAction(CreditCardAction.ExpirationDateChanged(it))
                        }
                    },
                    fieldName = stringResource(R.string.expiration_date),
                    visualTransformation = ExpirationDateMask(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(TrackizerTheme.spacing.medium))
                TrackizerTextField(
                    text = state.cvv,
                    onTextChange = {
                        if (it.length <= CardConstants.CVV_LENGTH) {
                            onAction(CreditCardAction.CvvCodeChanged(it))
                        }
                    },
                    fieldName = stringResource(R.string.cvv),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                    modifier = Modifier.weight(1f),
                )
            }
            Spacer(Modifier.height(TrackizerTheme.spacing.large))
            TrackizerButton(
                text = buttonText,
                type = ButtonType.Primary,
                onClick = { onAction(CreditCardAction.SaveCard) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
