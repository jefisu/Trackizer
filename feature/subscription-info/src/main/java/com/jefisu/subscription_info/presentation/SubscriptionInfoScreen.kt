@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.jefisu.subscription_info.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.designsystem.Black23
import com.jefisu.designsystem.BorderBrush
import com.jefisu.designsystem.Gray100
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.SubscriptionIcon
import com.jefisu.designsystem.components.TrackizerAlertBottomSheet
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.formatCurrency
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.subscription_info.R
import com.jefisu.subscription_info.presentation.components.CustomDivider
import com.jefisu.subscription_info.presentation.components.InfoRowItem
import com.jefisu.subscription_info.presentation.components.SubscriptionInfoBottomSheet
import com.jefisu.subscription_info.presentation.util.InfoRow
import com.jefisu.subscription_info.presentation.util.InfoRowType
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.ext.toDateFormat
import com.jefisu.ui.screen.LocalScreenIsSmall
import com.jefisu.ui.util.SampleData

@Composable
internal fun SubscriptionInfoScreen(
    state: SubscriptionInfoState,
    onAction: (SubscriptionInfoAction) -> Unit,
) {
    val isSmallScreen = LocalScreenIsSmall.current
    val scaleScreen = if (isSmallScreen) 0.8f else 1f
    val horizontalSpacingInfoRow = 20.dp

    val deleteSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    TrackizerAlertBottomSheet(
        sheetState = deleteSheetState,
        title = stringResource(
            id = UiRes.string.delete_alert_title,
            stringResource(UiRes.string.subscription).lowercase(),
        ),
        description = stringResource(
            id = UiRes.string.delete_alert_description,
            stringResource(UiRes.string.subscription).lowercase(),
        ),
        onDismissTextButton = stringResource(UiRes.string.button_alert_cancel),
        onConfirmTextButton = stringResource(UiRes.string.button_alert_delete),
        onDismiss = {},
        onConfirm = {
            onAction(SubscriptionInfoAction.DeleteSubscription)
        },
    )

    val unsavedChangesSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    TrackizerAlertBottomSheet(
        sheetState = unsavedChangesSheetState,
        title = stringResource(R.string.confirm_changes),
        description = stringResource(R.string.do_you_want_to_exit_without_save_changes),
        onDismissTextButton = stringResource(R.string.discard),
        onConfirmTextButton = stringResource(UiRes.string.save),
        onDismiss = {
            onAction(SubscriptionInfoAction.NavigateBack)
        },
        onConfirm = {
            onAction(SubscriptionInfoAction.SaveSubscription)
        },
    )

    val infoSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    SubscriptionInfoBottomSheet(
        sheetState = infoSheetState,
        state = state,
        onAction = onAction,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray100)
            .systemBarsPadding(),
    ) {
        state.subscription?.let { subscription ->
            val infoRows = listOf(
                InfoRow(
                    value = subscription.service.title,
                    label = null,
                    type = InfoRowType.Name,
                ),
                InfoRow(
                    value = subscription.description,
                    label = stringResource(R.string.no_data),
                    type = InfoRowType.Description,
                ),
                InfoRow(
                    value = subscription.category?.name.orEmpty(),
                    label = stringResource(id = R.string.no_data),
                    type = InfoRowType.Category,
                ),
                InfoRow(
                    value = subscription.firstPayment.toDateFormat(),
                    label = stringResource(R.string.first_payment),
                    type = InfoRowType.FirstPayment,
                ),
                InfoRow(
                    value = stringResource(
                        if (subscription.reminder) R.string.forever else R.string.never,
                    ),
                    label = stringResource(R.string.never),
                    type = InfoRowType.Reminder,
                ),
                InfoRow(
                    value = subscription.card?.number?.takeLast(4).orEmpty(),
                    label = stringResource(id = R.string.no_data),
                    type = InfoRowType.CreditCard,
                ),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = TrackizerTheme.spacing.extraMedium,
                        vertical = if (isSmallScreen) TrackizerTheme.spacing.small else TrackizerTheme.spacing.extraMedium,
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Black23),
            ) {
                TrackizerTopBar(
                    title = stringResource(R.string.subscription_info),
                    navigationIcon = {
                        TrackizerTopBarDefaults.backNavigationIcon(
                            onClick = {
                                if (state.hasUnsavedChanges) {
                                    unsavedChangesSheetState.currentDetent =
                                        SheetDetent.FullyExpanded
                                    return@backNavigationIcon
                                }
                                onAction(SubscriptionInfoAction.NavigateBack)
                            },
                            modifier = Modifier.rotate(270f),
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                deleteSheetState.currentDetent = SheetDetent.FullyExpanded
                            },
                        ) {
                            Icon(
                                painter = painterResource(DesignSystemRes.drawable.ic_trash),
                                contentDescription = "Delete subscription",
                            )
                        }
                    },
                    modifier = Modifier.padding(
                        horizontal = TrackizerTheme.spacing.medium,
                        vertical = if (isSmallScreen) 0.dp else TrackizerTheme.spacing.small,
                    ),
                )
                SubscriptionIcon(
                    icon = subscription.service,
                    containerSize = 106.dp * scaleScreen,
                    iconSize = 61.dp * scaleScreen,
                    cornerSize = 30.dp * scaleScreen,
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.medium))
                Text(
                    text = subscription.service.title,
                    style = TrackizerTheme.typography.headline6.copy(
                        fontSize = TrackizerTheme.typography.headline6.fontSize * scaleScreen,
                    ),
                    modifier = Modifier.basicMarquee(),
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.small))
                Text(
                    text = formatCurrency(
                        value = subscription.price.toDouble(),
                        isCompactFormat = false,
                    ),
                    style = TrackizerTheme.typography.headline4,
                    color = Gray30,
                )
                Spacer(
                    Modifier.height(
                        if (isSmallScreen) TrackizerTheme.spacing.small else TrackizerTheme.spacing.large,
                    ),
                )
                CustomDivider()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Gray70)
                        .padding(
                            horizontal = horizontalSpacingInfoRow,
                            vertical = if (isSmallScreen) TrackizerTheme.spacing.small else TrackizerTheme.spacing.medium,
                        ),
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(1f)
                            .clip(Shapes().large)
                            .background(Gray60.copy(0.2f))
                            .border(
                                width = 1.dp,
                                shape = Shapes().large,
                                brush = BorderBrush,
                            ),
                    ) {
                        infoRows.forEach { info ->
                            InfoRowItem(
                                info = info,
                                modifier = Modifier
                                    .weight(1f)
                                    .rippleClickable {
                                        if (info.type == InfoRowType.Name) return@rippleClickable
                                        onAction(
                                            SubscriptionInfoAction.ToggleSubscriptionInfoSheet(
                                                infoRow = info,
                                            ),
                                        )
                                        infoSheetState.currentDetent = SheetDetent.FullyExpanded
                                    }
                                    .padding(horizontal = horizontalSpacingInfoRow),
                            )
                        }
                    }
                    Spacer(
                        Modifier.height(
                            if (isSmallScreen) TrackizerTheme.spacing.medium else TrackizerTheme.spacing.large,
                        ),
                    )
                    TrackizerButton(
                        text = stringResource(UiRes.string.save),
                        type = ButtonType.Secondary,
                        onClick = {
                            onAction(SubscriptionInfoAction.SaveSubscription)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun SubscriptionInfoScreenPreview() {
    TrackizerTheme {
        SubscriptionInfoScreen(
            state = SubscriptionInfoState(
                subscription = SampleData.subscriptions.first(),
            ),
            onAction = {},
        )
    }
}
