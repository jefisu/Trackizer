package com.jefisu.user.presentation.editprofile

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.sharedtransition.LocalAnimatedVisibilityScope

fun NavGraphBuilder.editProfileScreen() = composable<Destination.EditProfileScreen> {
    val viewModel = hiltViewModel<EditProfileViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CompositionLocalProvider(
        LocalAnimatedVisibilityScope provides this,
    ) {
        EditProfileScreen(
            state = state,
            onAction = viewModel::onAction,
        )
    }
}