package com.jefisu.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        for (i in text.indices) {
            AnimatedContent(
                targetState = text[i],
                transitionSpec = {
                    val tweenDuration = 700
                    slideInVertically(
                        animationSpec = tween(durationMillis = tweenDuration),
                        initialOffsetY = { it },
                    ) togetherWith slideOutVertically(
                        animationSpec = tween(durationMillis = tweenDuration),
                        targetOffsetY = { -it },
                    )
                },
                label = "",
            ) { char ->
                Text(
                    text = char.toString(),
                    style = style,
                    softWrap = false,
                )
            }
        }
    }
}