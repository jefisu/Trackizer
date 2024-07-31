package com.jefisu.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.home.R
import com.jefisu.home.presentation.components.Header
import com.jefisu.home.presentation.components.HorizontalTabs
import com.jefisu.home.presentation.components.SubscriptionList
import com.jefisu.home.presentation.components.SubscriptionTab
import com.jefisu.home.presentation.util.SampleData
import com.jefisu.home.presentation.util.filterUpcomingBills
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Theme

@Composable
fun HomeScreen(
    state: HomeState,
    onNavigateToSettings: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Header(
            state = state,
            onSettingsClick = onNavigateToSettings,
            onSeeBudgetClick = { },
        )
        Spacer(modifier = Modifier.height(20.dp))
        HorizontalTabs(
            modifier = Modifier.padding(horizontal = Theme.spacing.extraMedium),
            tabContent = { tab ->
                when (tab) {
                    SubscriptionTab.YOUR_SUBSCRIPTIONS -> {
                        SubscriptionList(
                            subscriptions = state.subscriptions,
                            messageEmptyList = stringResource(
                                R.string.you_don_t_have_any_subscriptions,
                            ),
                        )
                    }

                    SubscriptionTab.UPCOMING_BILLS -> {
                        val subscriptionActive = remember {
                            state.subscriptions.filterUpcomingBills()
                        }
                        SubscriptionList(
                            subscriptions = subscriptionActive,
                            upcomingBill = true,
                            messageEmptyList = stringResource(
                                R.string.you_don_t_have_any_upcoming_bills,
                            ),
                        )
                    }
                }
            },
        )
    }
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        HomeScreen(
            state = HomeState(
                monthlyBudget = SampleData.monthlyBudget,
                subscriptions = SampleData.subscriptions,
            ),
        )
    }
}
