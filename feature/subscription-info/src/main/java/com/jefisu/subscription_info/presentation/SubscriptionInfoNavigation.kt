package com.jefisu.subscription_info.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionInfoRoute(val id: String)

fun NavController.navigateToSubscriptionInfo(id: String) {
    navigate(SubscriptionInfoRoute(id))
}

fun NavGraphBuilder.subscriptionInfoScreen() {
    val animationSpec = tween<IntOffset>(durationMillis = 700)
    composable<SubscriptionInfoRoute>(
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

        SubscriptionInfoScreen(
            state = viewModel.state,
            onAction = viewModel::onAction,
        )
    }
}
