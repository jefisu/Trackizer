@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.settings.presentation.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.domain.model.User
import com.jefisu.settings.R
import com.jefisu.ui.sharedtransition.SharedTransitionsKeys
import com.jefisu.ui.sharedtransition.sharedTransition
import com.jefisu.ui.util.SampleData

@Composable
internal fun UserProfile(
    user: User,
    modifier: Modifier = Modifier,
) {
    val isPreviewMode = LocalInspectionMode.current
    val imageShape = CircleShape

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        AsyncImage(
            model = user.pictureUrl,
            contentDescription = user.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .sharedTransition { animatedVisibilityScope ->
                    Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(
                            key = SharedTransitionsKeys.USER_PROFILE_IMAGE,
                        ),
                        animatedVisibilityScope = animatedVisibilityScope,
                        clipInOverlayDuringTransition = OverlayClip(imageShape),
                    )
                }
                .size(72.dp)
                .clip(imageShape)
                .paint(
                    painter = painterResource(R.drawable.user_picture_profile),
                    alpha = if (user.pictureUrl == null || isPreviewMode) 1f else 0f,
                ),
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.medium))
        Text(
            text = user.name.orEmpty(),
            style = TrackizerTheme.typography.headline4,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = user.email,
            style = TrackizerTheme.typography.bodySmall,
            color = Gray30,
        )
    }
}

@Preview
@Composable
private fun UserProfilePreview() {
    TrackizerTheme {
        UserProfile(
            user = SampleData.user,
            modifier = Modifier.padding(TrackizerTheme.spacing.medium),
        )
    }
}