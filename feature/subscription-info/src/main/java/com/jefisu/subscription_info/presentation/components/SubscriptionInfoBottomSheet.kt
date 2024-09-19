@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.subscription_info.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.DatePickerState
import com.jefisu.designsystem.components.LabeledCheckbox
import com.jefisu.designsystem.components.PickerState
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerDatePicker
import com.jefisu.designsystem.components.TrackizerPicker
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.components.hideSheet
import com.jefisu.designsystem.components.rememberPickerState
import com.jefisu.designsystem.components.rememberTrackizerDatePickerState
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.domain.model.Card
import com.jefisu.domain.model.Category
import com.jefisu.subscription_info.R
import com.jefisu.subscription_info.presentation.SubscriptionInfoAction
import com.jefisu.subscription_info.presentation.SubscriptionInfoState
import com.jefisu.subscription_info.presentation.util.InfoRowType
import com.jefisu.ui.util.asIconResource

@Composable
fun SubscriptionInfoBottomSheet(
    state: SubscriptionInfoState,
    onAction: (SubscriptionInfoAction) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    state.subscription?.let { subscription ->
        val cardPickerState = rememberPickerState<Card>()
        val categoryPickerState = rememberPickerState<Category>()
        val datePickerState = rememberTrackizerDatePickerState(
            initialDate = subscription.paymentDate,
        )

        TrackizerBottomSheet(
            isVisible = state.showSubscriptionInfoSheet,
            sheetState = sheetState,
            horizontalAligment = Alignment.Start,
            onDismiss = { onAction(SubscriptionInfoAction.ToggleSubscriptionInfoSheet()) },
        ) {
            BottomSheetContent(
                state = state,
                cardPickerState = cardPickerState,
                categoryPickerState = categoryPickerState,
                datePickerState = datePickerState,
                sheetState = sheetState,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    state: SubscriptionInfoState,
    cardPickerState: PickerState<Card>,
    categoryPickerState: PickerState<Category>,
    datePickerState: DatePickerState,
    sheetState: SheetState,
    onAction: (SubscriptionInfoAction) -> Unit,
) {
    val scope = rememberCoroutineScope()

    state.selectedInfoRow?.let { infoRow ->
        when (infoRow.type) {
            InfoRowType.Description -> {
                TrackizerTextField(
                    text = state.description,
                    onTextChange = {
                        onAction(SubscriptionInfoAction.DescriptionChanged(it))
                    },
                    fieldName = stringResource(R.string.description),
                )
            }

            InfoRowType.Category -> {
                CategoryPicker(
                    categories = state.categories,
                    state = categoryPickerState,
                    initialCategory = state.subscription?.category,
                )
            }

            InfoRowType.FirstPayment -> {
                TrackizerDatePicker(
                    state = datePickerState,
                )
            }

            InfoRowType.Reminder -> {
                LabeledCheckbox(
                    isChecked = state.reminder,
                    onCheckedChange = {
                        onAction(SubscriptionInfoAction.ReminderChanged(it))
                    },
                    label = stringResource(R.string.reminder_for_the_upcoming_months),
                )
            }

            InfoRowType.CreditCard -> {
                CreditCardPicker(
                    creditCards = state.creditCards,
                    state = cardPickerState,
                    initialCreditCard = state.subscription?.card,
                )
            }

            else -> Unit
        }
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        TrackizerButton(
            text = when (infoRow.type) {
                InfoRowType.Description -> stringResource(R.string.save)
                else -> stringResource(R.string.select)
            },
            type = ButtonType.Primary,
            onClick = {
                when (state.selectedInfoRow.type) {
                    InfoRowType.Description -> onAction(
                        SubscriptionInfoAction.DescriptionChanged(
                            state.description,
                            applyChanges = true,
                        ),
                    )

                    InfoRowType.Category -> onAction(
                        SubscriptionInfoAction.CategoryChanged(
                            categoryPickerState.selectedItem!!,
                        ),
                    )

                    InfoRowType.FirstPayment -> onAction(
                        SubscriptionInfoAction.FirstPaymentChanged(
                            datePickerState.date,
                        ),
                    )

                    InfoRowType.Reminder -> onAction(
                        SubscriptionInfoAction.ReminderChanged(
                            state.reminder,
                            applyChanges = true,
                        ),
                    )

                    InfoRowType.CreditCard -> onAction(
                        SubscriptionInfoAction.CreditCardChanged(
                            cardPickerState.selectedItem!!,
                        ),
                    )

                    else -> Unit
                }
                sheetState.hideSheet(
                    scope = scope,
                    onDismiss = {
                        onAction(SubscriptionInfoAction.ToggleSubscriptionInfoSheet())
                    },
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun CategoryPicker(
    initialCategory: Category?,
    categories: List<Category>,
    state: PickerState<Category>,
) {
    Text(
        text = stringResource(R.string.select_a_category),
        style = TrackizerTheme.typography.headline3,
    )
    Spacer(Modifier.height(TrackizerTheme.spacing.medium))
    TrackizerPicker(
        items = categories,
        state = state,
        startIndex = categories.indexOf(initialCategory),
    ) { category ->
        CategoryItem(category)
    }
}

@Composable
private fun CategoryItem(category: Category) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            painter = painterResource(category.type.asIconResource()),
            contentDescription = category.name,
            modifier = Modifier.size(32.dp),
        )
        Spacer(Modifier.width(TrackizerTheme.spacing.small))
        Text(
            text = category.name,
            style = TrackizerTheme.typography.headline4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun CreditCardPicker(
    initialCreditCard: Card?,
    creditCards: List<Card>,
    state: PickerState<Card>,
) {
    Text(
        text = stringResource(R.string.select_a_credit_card),
        style = TrackizerTheme.typography.headline3,
    )
    Spacer(Modifier.height(TrackizerTheme.spacing.medium))
    TrackizerPicker(
        items = creditCards,
        state = state,
        startIndex = creditCards.indexOf(initialCreditCard),
    ) { card ->
        CreditCardItem(card)
    }
}

@Composable
private fun CreditCardItem(card: Card) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = TrackizerTheme.spacing.small,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = TrackizerTheme.spacing.small),
    ) {
        Text(
            text = card.flag.name,
            style = TrackizerTheme.typography.headline4,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(
            Modifier
                .size(4.dp)
                .drawBehind {
                    drawCircle(color = Color.White)
                },
        )
        Text(
            text = card.number.takeLast(4),
            style = TrackizerTheme.typography.headline4,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
