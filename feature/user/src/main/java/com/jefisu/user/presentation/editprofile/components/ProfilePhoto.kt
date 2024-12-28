@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.jefisu.user.presentation.editprofile.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.ui.sharedtransition.SharedTransitionsKeys
import com.jefisu.ui.sharedtransition.sharedTransition
import com.jefisu.user.R

@Composable
fun ProfilePhoto(
    photo: Any?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val isPreviewMode = LocalInspectionMode.current
    val imageModifier = Modifier
        .size(140.dp)
        .clip(CircleShape)
        .rippleClickable(
            enabled = onClick != null,
            onClick = { onClick?.invoke() },
        )

    val rotateAnim = remember { Animatable(0f) }
    var previousPhoto = remember { photo }
    LaunchedEffect(photo) {
        if (photo != null && !rotateAnim.isRunning && photo != previousPhoto) {
            rotateAnim.animateTo(
                targetValue = if (rotateAnim.targetValue == 0f) 360f else 0f,
                animationSpec = tween(
                    durationMillis = 700,
                ),
            )
            previousPhoto = photo
        }
    }
    Crossfade(
        targetState = photo,
        label = "crossfade",
        animationSpec = tween(
            durationMillis = 500,
        ),
        modifier = modifier
            .sharedTransition { animatedVisibilityScope ->
                Modifier.sharedBounds(
                    sharedContentState = rememberSharedContentState(
                        key = SharedTransitionsKeys.USER_PROFILE_IMAGE,
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                    clipInOverlayDuringTransition = OverlayClip(CircleShape),
                )
            }
            .graphicsLayer {
                rotationY = rotateAnim.value
            },
    ) { photoDisplay ->
        photoDisplay?.let {
            Box(
                modifier = Modifier.drawWithContent {
                    drawContent()
                    drawCircle(color = Gray80.copy(alpha = 0.4f))
                },
            ) {
                AsyncImage(
                    model = it,
                    contentDescription = it.toString(),
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier.then(
                        if (isPreviewMode && it is Int) {
                            Modifier.paint(
                                painter = painterResource(it),
                                contentScale = ContentScale.Crop,
                            )
                        } else {
                            Modifier
                        },
                    ),
                )
            }
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = null,
                modifier = imageModifier.scale(0.35f),
            )
        } ?: run {
            Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = null,
                modifier = imageModifier
                    .background(AccentPrimary100.copy(alpha = 0.3f))
                    .scale(0.7f),
            )
        }
    }
}

private class ProfilePhotoPreviewParameter : PreviewParameterProvider<Int?> {
    override val values: Sequence<Int?> = sequenceOf(
        null,
        R.drawable.user_photo_preview,
    )
}

@Preview
@Composable
private fun ProfilePhotoPreview(
    @PreviewParameter(ProfilePhotoPreviewParameter::class) photoId: Int?,
) {
    TrackizerTheme {
        ProfilePhoto(
            photo = photoId,
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        )
    }
}