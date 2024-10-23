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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Black23
import com.jefisu.designsystem.BorderBrush
import com.jefisu.designsystem.Gray100
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Gray70
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
import com.jefisu.ui.ext.toDateFormat
import com.jefisu.ui.util.SampleData
import com.jefisu.designsystem.R as DesignSystemRes

@Composable
internal fun SubscriptionInfoScreen(
    state: SubscriptionInfoState,
    onAction: (SubscriptionInfoAction) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val horizontalSpacingInfoRow = 20.dp

    TrackizerAlertBottomSheet(
        isVisible = state.showDeleteAlert,
        title = stringResource(R.string.delete_subscription),
        description = stringResource(R.string.are_you_sure_you_want_to_delete_this_subscription),
        onDismissTextButton = stringResource(R.string.cancel),
        onConfirmTextButton = stringResource(R.string.delete),
        onDismiss = {
            onAction(SubscriptionInfoAction.ToogleDeleteAlert)
        },
        onConfirm = {
            onAction(SubscriptionInfoAction.DeleteSubscription)
        },
    )

    TrackizerAlertBottomSheet(
        isVisible = state.showUnsavedChangesAlert,
        title = stringResource(R.string.confirm_changes),
        description = stringResource(R.string.do_you_want_to_exit_without_save_changes),
        onDismissTextButton = stringResource(R.string.discard),
        onConfirmTextButton = stringResource(R.string.save),
        onDismiss = {
            onAction(SubscriptionInfoAction.ToogleUnsavedChangesAlert)
            onAction(SubscriptionInfoAction.NavigateBack)
        },
        onConfirm = {
            onAction(SubscriptionInfoAction.SaveSubscription)
        },
    )

    SubscriptionInfoBottomSheet(
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
                    label = stringResource(R.string.no_description),
                    type = InfoRowType.Description,
                ),
                InfoRow(
                    value = subscription.category?.name.orEmpty(),
                    label = stringResource(R.string.no_category),
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
                    label = stringResource(R.string.no_credit_card),
                    type = InfoRowType.CreditCard,
                ),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(TrackizerTheme.spacing.extraMedium)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Black23),
            ) {
                TrackizerTopBar(
                    title = stringResource(R.string.subscription_info),
                    navigationIcon = {
                        TrackizerTopBarDefaults.backNavigationIcon(
                            onClick = {
                                if (state.hasUnsavedChanges) {
                                    onAction(SubscriptionInfoAction.ToogleUnsavedChangesAlert)
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
                                onAction(SubscriptionInfoAction.ToogleDeleteAlert)
                            },
                        ) {
                            Icon(
                                painter = painterResource(DesignSystemRes.drawable.ic_trash),
                                contentDescription = "Delete subscription",
                            )
                        }
                    },
                    modifier = Modifier.padding(TrackizerTheme.spacing.medium),
                )
                SubscriptionIcon(
                    icon = subscription.service,
                    containerSize = 106.dp,
                    iconSize = 61.dp,
                    cornerSize = 30.dp,
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.medium))
                Text(
                    text = subscription.service.title,
                    style = TrackizerTheme.typography.headline6,
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
                Spacer(Modifier.height(TrackizerTheme.spacing.large))
                CustomDivider()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Gray70)
                        .padding(
                            horizontal = horizontalSpacingInfoRow,
                            vertical = TrackizerTheme.spacing.medium,
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
                                        onAction(
                                            SubscriptionInfoAction.ToggleSubscriptionInfoSheet(
                                                info,
                                            ),
                                        )
                                    }
                                    .padding(horizontal = horizontalSpacingInfoRow),
                            )
                        }
                    }
                    Spacer(Modifier.height(TrackizerTheme.spacing.large))
                    TrackizerButton(
                        text = stringResource(R.string.save),
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
