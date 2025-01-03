@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.subscription_info.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.core.ModalBottomSheetState
import com.composables.core.SheetDetent
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.DatePickerState
import com.jefisu.designsystem.components.LabeledCheckbox
import com.jefisu.designsystem.components.TrackizerBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerDatePicker
import com.jefisu.designsystem.components.TrackizerPicker
import com.jefisu.designsystem.components.TrackizerPickerDefaults
import com.jefisu.designsystem.components.TrackizerPickerState
import com.jefisu.designsystem.components.TrackizerTextField
import com.jefisu.designsystem.components.rememberTrackizerDatePickerState
import com.jefisu.designsystem.components.rememberTrackizerPickerState
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.asIconResource
import com.jefisu.domain.model.Card
import com.jefisu.domain.model.Category
import com.jefisu.subscription_info.R
import com.jefisu.subscription_info.presentation.SubscriptionInfoAction
import com.jefisu.subscription_info.presentation.SubscriptionInfoEvent
import com.jefisu.subscription_info.presentation.SubscriptionInfoState
import com.jefisu.subscription_info.presentation.util.InfoRowType
import com.jefisu.ui.ObserveAsEvents
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.UiEventController

@Composable
fun SubscriptionInfoBottomSheet(
    sheetState: ModalBottomSheetState,
    state: SubscriptionInfoState,
    onAction: (SubscriptionInfoAction) -> Unit,
) {
    state.subscription?.let { subscription ->
        val datePickerState = rememberTrackizerDatePickerState(
            initialDate = subscription.firstPayment,
        )

        ObserveAsEvents(UiEventController.events) { event ->
            if (event is SubscriptionInfoEvent.ToogleBottomSheet) {
                sheetState.currentDetent = if (sheetState.currentDetent == SheetDetent.Hidden) {
                    SheetDetent.FullyExpanded
                } else {
                    SheetDetent.Hidden
                }
            }
        }

        TrackizerBottomSheet(
            sheetState = sheetState,
            horizontalAligment = Alignment.Start,
            onDismiss = { onAction(SubscriptionInfoAction.ToggleSubscriptionInfoSheet()) },
        ) {
            BottomSheetContent(
                state = state,
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
    datePickerState: DatePickerState,
    sheetState: ModalBottomSheetState,
    onAction: (SubscriptionInfoAction) -> Unit,
) {
    val categoryPickerState = rememberTrackizerPickerState(
        startIndex = state.categories.indexOf(state.subscription?.category),
        itemsCount = state.categories.size,
    )
    val cardPickerState = rememberTrackizerPickerState(
        startIndex = state.creditCards.indexOf(state.subscription?.card),
        itemsCount = state.creditCards.size,
    )

    state.selectedInfoRow?.let { infoRow ->
        when (infoRow.type) {
            InfoRowType.Description -> {
                TrackizerTextField(
                    text = state.description,
                    onTextChange = {
                        onAction(SubscriptionInfoAction.DescriptionChanged(it))
                    },
                    fieldName = stringResource(UiRes.string.description),
                )
            }

            InfoRowType.Category -> {
                PickerBody(
                    items = state.categories,
                    state = categoryPickerState,
                    title = stringResource(
                        id = R.string.select_a,
                        stringResource(UiRes.string.category).lowercase(),
                    ),
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
                PickerBody(
                    items = state.creditCards,
                    state = cardPickerState,
                    title = stringResource(
                        id = R.string.select_a,
                        stringResource(UiRes.string.credit_card).lowercase(),
                    ),
                )
            }

            else -> Unit
        }
        Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
        TrackizerButton(
            text = when (infoRow.type) {
                InfoRowType.Description -> stringResource(UiRes.string.save)
                else -> stringResource(UiRes.string.select)
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
                            state.categories[categoryPickerState.selectedIndex],
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
                            state.creditCards[cardPickerState.selectedIndex],
                        ),
                    )

                    else -> Unit
                }
                sheetState.currentDetent = SheetDetent.Hidden
                onAction(SubscriptionInfoAction.ToggleSubscriptionInfoSheet())
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun <T> PickerBody(
    items: List<T>,
    state: TrackizerPickerState,
    title: String,
) {
    Text(
        text = title,
        style = TrackizerTheme.typography.headline3,
    )
    Spacer(Modifier.height(TrackizerTheme.spacing.medium))
    TrackizerPicker(
        items = items,
        state = state,
    ) { item ->
        when (item) {
            is Category -> CategoryItem(item)
            is Card -> CreditCardItem(item)
        }
    }
}

@Composable
private fun CategoryItem(category: Category) {
    TrackizerPickerDefaults.PickerItem(
        text = category.name,
        leadingIcon = {
            Icon(
                painter = painterResource(category.type.asIconResource()),
                contentDescription = category.name,
                modifier = Modifier.size(32.dp),
            )
        },
    )
}

@Composable
private fun CreditCardItem(card: Card) {
    TrackizerPickerDefaults.PickerItem(
        text = card.number.takeLast(4),
        leadingIcon = {
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
        },
    )
}