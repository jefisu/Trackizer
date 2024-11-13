package com.jefisu.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

data class AnimationConfig(
    val destinations: List<Destination>,
    val type: AnimationType,
)

enum class AnimationType {
    HORIZONTAL,
    VERTICAL
}

class NavigationAnimation(
    private val animConfigs: List<AnimationConfig> = emptyList(),
    private val animDuration: Int = 700,
) {

    private fun getSlideDirection(
        isPopAnimation: Boolean,
        config: AnimationConfig,
    ): AnimatedContentTransitionScope.SlideDirection {
        return if (isPopAnimation) {
            when (config.type) {
                AnimationType.HORIZONTAL -> AnimatedContentTransitionScope.SlideDirection.Right
                AnimationType.VERTICAL -> AnimatedContentTransitionScope.SlideDirection.Down
            }
        } else {
            when (config.type) {
                AnimationType.HORIZONTAL -> AnimatedContentTransitionScope.SlideDirection.Left
                AnimationType.VERTICAL -> AnimatedContentTransitionScope.SlideDirection.Up
            }
        }
    }

    private fun <T> getAnimationConfig(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        targetAnim: (AnimationConfig) -> T,
        initialAnim: (AnimationConfig) -> T,
        defaultAnim: () -> T,
    ): T {
        animConfigs
            .firstOrNull { it.destinations.any(scope.targetState::isCurrentDestination) }
            ?.let { return targetAnim(it) }

        animConfigs
            .firstOrNull { it.destinations.any(scope.initialState::isCurrentDestination) }
            ?.let { return initialAnim(it) }

        return defaultAnim()
    }

    fun enterTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        isPopAnimation: Boolean = false,
    ): EnterTransition {
        return getAnimationConfig(
            scope = scope,
            targetAnim = { config ->
                scope.slideIntoContainer(
                    towards = getSlideDirection(isPopAnimation, config),
                    animationSpec = tween(animDuration),
                )
            },
            initialAnim = { config ->
                scope.slideIntoContainer(
                    towards = getSlideDirection(isPopAnimation, config),
                    animationSpec = tween(animDuration),
                )
            },
            defaultAnim = { fadeIn(animationSpec = tween(animDuration)) },
        )
    }

    fun exitTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        isPopAnimation: Boolean = false,
    ): ExitTransition {
        return getAnimationConfig(
            scope = scope,
            targetAnim = { config ->
                scope.slideOutOfContainer(
                    towards = getSlideDirection(isPopAnimation, config),
                    animationSpec = tween(animDuration),
                )
            },
            initialAnim = { config ->
                scope.slideOutOfContainer(
                    towards = getSlideDirection(isPopAnimation, config),
                    animationSpec = tween(animDuration),
                )
            },
            defaultAnim = { fadeOut(animationSpec = tween(animDuration)) },
        )
    }
}