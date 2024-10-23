package com.jefisu.calendar.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.calendarScreen() {
    composable<Destination.CalendarScreen> {
        val viewModel = hiltViewModel<CalendarViewModel>()

        CalendarScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
        )
    }
}
