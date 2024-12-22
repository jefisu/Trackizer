@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.calendar.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.jefisu.calendar.R
import com.jefisu.calendar.presentation.components.DayBadgeItem
import com.jefisu.calendar.presentation.components.DropDown
import com.jefisu.calendar.presentation.components.MonthPickerBottomSheet
import com.jefisu.calendar.presentation.components.ScheduledSubscriptionItem
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.AnimatedText
import com.jefisu.designsystem.components.TrackizerBottomNavigation
import com.jefisu.designsystem.components.TrackizerScaffold
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.LocalSettings
import com.jefisu.designsystem.util.formatCurrency
import com.jefisu.domain.model.Subscription
import com.jefisu.domain.model.util.filterUpcomingBills
import com.jefisu.ui.ext.formatMonthName
import com.jefisu.ui.ext.toDateFormat
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.screen.LocalScreenIsSmall
import com.jefisu.ui.util.SampleData
import java.time.LocalDate

@Composable
internal fun CalendarScreen(
    state: CalendarState,
    onAction: (CalendarAction) -> Unit,
) {
    val upcomingBills = remember(state.subscriptions, state.selectedDay) {
        state.subscriptions.filterUpcomingBills(
            currentDate = state.selectedDay,
            isPerDay = true,
        )
    }
    val total = upcomingBills.sumOf { it.price.toDouble() }
    val settings = LocalSettings.current
    val isSmallScreen = LocalScreenIsSmall.current

    TrackizerScaffold(
        topBar = {
            TrackizerTopBar(
                title = stringResource(R.string.calendar_title),
                colors = TrackizerTopBarDefaults.colors.copy(
                    containerColor = Gray70,
                ),
                actions = {
                    TrackizerTopBarDefaults.settingsActionIcon(
                        onClick = { onAction(CalendarAction.Navigate(Destination.SettingsScreen)) },
                    )
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            SubscriptionSchedule(
                state = state,
                onAction = onAction,
                countSubscriptionsForToday = upcomingBills.size,
            )
            Spacer(
                modifier = Modifier.height(
                    if (isSmallScreen) TrackizerTheme.spacing.medium else TrackizerTheme.spacing.extraMedium,
                ),
            )
            CalendarRow(
                leftValue = state.selectedMonth.formatMonthName(
                    settings.toLanguageLocale(),
                ),
                rightValue = formatCurrency(total),
                style = TrackizerTheme.typography.headline4,
                isAnimatedCurrency = true,
                color = LocalContentColor.current,
                modifier = Modifier.padding(horizontal = TrackizerTheme.spacing.extraMedium),
            )
            Spacer(modifier = Modifier.height(4.dp))
            CalendarRow(
                leftValue = state.selectedDay.toDateFormat(),
                rightValue = stringResource(R.string.in_upcoming_bills),
                style = TrackizerTheme.typography.bodySmall,
                color = Gray30,
                modifier = Modifier.padding(horizontal = TrackizerTheme.spacing.extraMedium),
            )
            Spacer(
                modifier = Modifier.height(
                    if (isSmallScreen) TrackizerTheme.spacing.medium else TrackizerTheme.spacing.extraMedium,
                ),
            )
            ScheduledSubscriptionsPerDay(
                subscriptions = upcomingBills,
                onNavigate = {
                    onAction(CalendarAction.Navigate(Destination.SubscriptionInfoScreen(it.id)))
                },
            )
        }
    }
}

@Composable
private fun SubscriptionSchedule(
    state: CalendarState,
    onAction: (CalendarAction) -> Unit,
    countSubscriptionsForToday: Int,
) {
    val daysOfMonth = remember(state) {
        val totalDaysOfMonth = state.selectedMonth.lengthOfMonth()
        (1..totalDaysOfMonth).map { day: Int ->
            with(state.selectedMonth) { LocalDate.of(year, month, day) }
        }
    }
    val lazyListState = rememberLazyListState()
    val isSmallScreen = LocalScreenIsSmall.current

    LaunchedEffect(Unit) {
        val today = LocalDate.now()
        lazyListState.scrollToItem(
            daysOfMonth
                .indexOfFirst { it.isEqual(today) }
                .coerceAtLeast(0),
        )
    }

    val monthSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    MonthPickerBottomSheet(
        sheetState = monthSheetState,
        state = state,
        onAction = onAction,
    )

    Column(
        modifier = Modifier
            .background(Gray70, RoundedCornerShape(0.dp, 0.dp, 24.dp, 24.dp)),
    ) {
        Text(
            text = stringResource(R.string.subs_schedule),
            style = TrackizerTheme.typography.headline7,
            modifier = Modifier.padding(horizontal = TrackizerTheme.spacing.extraMedium),
        )
        Spacer(
            modifier = Modifier.height(
                if (isSmallScreen) {
                    TrackizerTheme.spacing.small
                } else {
                    TrackizerTheme.spacing.medium
                },
            ),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = TrackizerTheme.spacing.extraMedium),
        ) {
            AnimatedText(
                text = countSubscriptionsForToday.toString(),
                style = TrackizerTheme.typography.headline2.copy(
                    color = Gray30,
                ),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.subscriptions_for_today),
                color = Gray30,
                style = TrackizerTheme.typography.headline2,
                modifier = Modifier.weight(1f),
            )
            DropDown(
                text = state.selectedMonth.formatMonthName(),
                onClick = { monthSheetState.currentDetent = SheetDetent.FullyExpanded },
            )
        }
        LazyRow(
            state = lazyListState,
            contentPadding = PaddingValues(
                horizontal = TrackizerTheme.spacing.extraMedium,
                vertical = if (isSmallScreen) 20.dp else TrackizerTheme.spacing.extraMedium,
            ),
            horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
        ) {
            items(daysOfMonth) { day ->
                DayBadgeItem(
                    localDate = day,
                    selected = state.selectedDay.isEqual(day),
                    onClick = {
                        onAction(CalendarAction.SelectDay(day))
                    },
                )
            }
        }
    }
}

@Composable
private fun CalendarRow(
    leftValue: String,
    rightValue: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    isAnimatedCurrency: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = leftValue,
            style = style,
            color = color,
            modifier = Modifier.weight(1f),
        )
        if (isAnimatedCurrency) {
            AnimatedText(
                text = rightValue,
                style = style.copy(color = color),
            )
        } else {
            Text(
                text = rightValue,
                style = style,
                color = color,
            )
        }
    }
}

@Composable
fun ScheduledSubscriptionsPerDay(
    subscriptions: List<Subscription>,
    onNavigate: (Subscription) -> Unit,
) {
    val lazyGridState = rememberLazyGridState()
    val showDivider by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex > 0 ||
                lazyGridState.firstVisibleItemScrollOffset > 0
        }
    }

    Column {
        AnimatedVisibility(visible = showDivider) {
            HorizontalDivider(color = Gray50)
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = lazyGridState,
            verticalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
            horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
            contentPadding = PaddingValues(
                start = TrackizerTheme.spacing.extraMedium,
                end = TrackizerTheme.spacing.extraMedium,
                bottom = 100.dp,
            ),
        ) {
            items(
                items = subscriptions,
                key = { it.id },
            ) { subscription ->
                ScheduledSubscriptionItem(
                    subscription = subscription,
                    onClick = { onNavigate(subscription) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun CalendarScreenPreview() {
    TrackizerTheme {
        TrackizerBottomNavigation {
            CalendarScreen(
                state = CalendarState(
                    subscriptions = SampleData.subscriptions.map {
                        it.copy(firstPayment = LocalDate.now())
                    },
                ),
                onAction = {},
            )
        }
    }
}