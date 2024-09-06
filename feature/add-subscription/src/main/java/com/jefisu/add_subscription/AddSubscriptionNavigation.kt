package com.jefisu.add_subscription

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class AddSubscriptionRoute(val id: String?)

fun NavController.navigateAddSubscription(id: String? = null) {
    navigate(AddSubscriptionRoute(id))
}

fun NavGraphBuilder.addSubscriptionScreen() {
    val animationSpec = tween<IntOffset>(durationMillis = 700)

    composable<AddSubscriptionRoute>(
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
