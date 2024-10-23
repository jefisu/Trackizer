package com.jefisu.home.presentation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.homeScreen() {
    composable<Destination.HomeScreen> {
        val viewModel = hiltViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        HomeScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}
