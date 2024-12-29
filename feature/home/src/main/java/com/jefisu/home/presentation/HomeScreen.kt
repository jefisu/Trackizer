@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.TrackizerBottomNavigation
import com.jefisu.designsystem.components.TrackizerScaffold
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.spacing
import com.jefisu.domain.model.util.filterUpcomingBills
import com.jefisu.home.R
import com.jefisu.home.presentation.components.HorizontalTabs
import com.jefisu.home.presentation.components.SubscriptionDashboard
import com.jefisu.home.presentation.components.SubscriptionList
import com.jefisu.home.presentation.components.SubscriptionTab
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.screen.LocalScreenIsSmall
import com.jefisu.ui.util.SampleData

@Composable
fun HomeScreen() {
    val viewModel = hiltViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun HomeScreenContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val isSmallScreen = LocalScreenIsSmall.current

    TrackizerScaffold(
        topBar = {
            TrackizerTopBar(
                title = null,
                actions = {
                    TrackizerTopBarDefaults.settingsActionIcon(
                        onClick = {
                            onAction(HomeAction.Navigate(Destination.SettingsScreen))
                        },
                    )
                },
            )
        },
    ) {
        Column {
            SubscriptionDashboard(
                state = state,
                onSeeBudgetClick = {
                    onAction(HomeAction.Navigate(Destination.SpendingBudgetsScreen))
                },
            )
            Spacer(
                modifier = Modifier.height(
                    if (isSmallScreen) {
                        TrackizerTheme.spacing.extraSmall
                    } else {
                        TrackizerTheme.spacing.medium
                    },
                ),
            )
            HorizontalTabs(
                modifier = Modifier.padding(horizontal = TrackizerTheme.spacing.extraMedium),
            ) { tab ->
                when (tab) {
                    SubscriptionTab.YOUR_SUBSCRIPTIONS -> {
                        SubscriptionList(
                            subscriptions = state.subscriptions,
                            messageEmptyList = stringResource(
                                id = R.string.you_don_t_have_any,
                                stringResource(UiRes.string.subscriptions).lowercase(),
                            ),
                            onItemClick = {
                                onAction(
                                    HomeAction.Navigate(Destination.SubscriptionInfoScreen(it.id)),
                                )
                            },
                        )
                    }

                    SubscriptionTab.UPCOMING_BILLS -> {
                        val subscriptionActive = remember(state.subscriptions) {
                            state.subscriptions.filterUpcomingBills()
                        }
                        SubscriptionList(
                            subscriptions = subscriptionActive,
                            upcomingBill = true,
                            messageEmptyList = stringResource(
                                R.string.you_don_t_have_any,
                                stringResource(R.string.upcoming_bills_tab).lowercase(),
                            ),
                            onItemClick = {
                                onAction(
                                    HomeAction.Navigate(Destination.SubscriptionInfoScreen(it.id)),
                                )
                            },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    TrackizerTheme {
        TrackizerBottomNavigation {
            HomeScreenContent(
                state = HomeState(
                    monthlyBudget = SampleData.MONTHLY_BUDGET,
                    subscriptions = SampleData.subscriptions,
                ),
                onAction = { },
            )
        }
    }
}