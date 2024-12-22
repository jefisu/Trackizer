package com.jefisu.add_subscription.presentation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.addSubscriptionScreen() {
    composable<Destination.AddSubscriptionScreen> {
        val viewModel = hiltViewModel<AddSubscriptionViewModel>()

        AddSubscriptionScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
        )
    }
}