package com.jefisu.credit_cards.presentation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.creditCardScreen() {
    composable<Destination.CreditCardScreen> {
        val viewModel = hiltViewModel<CreditCardViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()

        CreditCardsScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}