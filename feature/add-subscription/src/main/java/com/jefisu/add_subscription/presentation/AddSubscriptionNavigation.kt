package com.jefisu.add_subscription.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination

fun NavGraphBuilder.addSubscriptionScreen() {
    val animationSpec = tween<IntOffset>(durationMillis = 700)

    composable<Destination.AddSubscriptionScreen>(
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
        val viewModel = hiltViewModel<AddSubscriptionViewModel>()

        AddSubscriptionScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
        )
    }
}
