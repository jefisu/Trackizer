package com.jefisu.subscription_info.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.subscriptionInfoScreen() {
    val animationSpec = tween<IntOffset>(durationMillis = 700)
    composable<Destination.SubscriptionInfoScreen>(
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = animationSpec,
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Down,
                animationSpec = animationSpec,
            )
        },
    ) {
        val viewModel = hiltViewModel<SubscriptionInfoViewModel>()
        val state = viewModel.state

        LaunchedEffect(state.subscription) {
            viewModel.checkDataChanges()
        }

        SubscriptionInfoScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}