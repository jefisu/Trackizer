package com.jefisu.calendar.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object CalendarScreen

fun NavGraphBuilder.calendarScreen(onNavigateToSettings: () -> Unit) {
    composable<CalendarScreen> {
        val viewModel = hiltViewModel<CalendarViewModel>()

        CalendarScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
            onNavigateToSettings = onNavigateToSettings,
            onNavigateToDetails = {}
        )
    }
}
