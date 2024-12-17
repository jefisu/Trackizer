package com.jefisu.credit_cards.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.credit_cards.R
import com.jefisu.designsystem.TrackizerTheme

@Composable
fun SlidingArrowAnimation(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val offsetXAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = with(LocalDensity.current) { 16.dp.toPx() },
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Icon(
        painter = painterResource(R.drawable.ic_arrow_right),
        contentDescription = null,
        tint = color,
        modifier = modifier
            .graphicsLayer {
                translationX = offsetXAnim
            },
    )
}

@Preview
@Composable
private fun SlidingArrowAnimationPreview() {
    TrackizerTheme {
        SlidingArrowAnimation()
    }
}