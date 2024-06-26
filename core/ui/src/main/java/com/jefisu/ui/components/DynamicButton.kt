package com.jefisu.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jefisu.ui.theme.AccentPrimary100
import com.jefisu.ui.theme.Theme
import com.jefisu.ui.util.dropShadow

private val buttonHeight = 48.dp

@Composable
fun DynamicButton(
    text: String,
    buttonType: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes leadingIconRes: Int? = null
) {
    val containerColor = when (buttonType) {
        is ButtonType.Primary -> AccentPrimary100
        is ButtonType.Secondary -> Color.White.copy(0.1f)
        is ButtonType.DynamicColor -> buttonType.containerColor
    }
    val contentColor = if (buttonType is ButtonType.DynamicColor) {
        buttonType.contentColor
    } else {
        LocalContentColor.current
    }

    Surface(
        onClick = onClick,
        color = containerColor,
        contentColor = contentColor,
        shape = CircleShape,
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .then(
                if (buttonType is ButtonType.Secondary) {
                    Modifier
                } else {
                    Modifier.dropShadow(
                        shape = CircleShape,
                        color = containerColor.copy(alpha = 0.5f),
                        blur = 25.dp,
                        offsetX = 8.dp
                    )
                }
            )
            .border(
                width = 1.dp,
                brush = Brush.sweepGradient(
                    colors = when (buttonType) {
                        is ButtonType.Primary -> listOf(
                            Color.White.copy(0.1f),
                            Color.White.copy(0.11f),
                            Color.White.copy(0.15f),
                            Color.White.copy(0.15f),
                            Color.White.copy(0.15f),
                            Color.White.copy(0.3f),
                            Color.White.copy(0.4f),
                            Color.White.copy(0.5f),
                            Color.White.copy(0.6f),
                            Color.White.copy(0f),
                        )

                        else -> listOf(
                            Color.White.copy(0.01f),
                            Color.White.copy(0.1f),
                            Color.White.copy(0f)
                        )
                    }
                ),
                shape = CircleShape
            )
    ) {
        Row(
            modifier = Modifier.wrapContentSize(),
        ) {
            leadingIconRes?.let { resId ->
                Icon(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(Theme.size.icon.extraSmall)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(Theme.spacing.small))
            }
            Text(
                text = text,
                style = Theme.typography.headline2
            )
        }
    }
}

sealed interface ButtonType {
    data object Primary : ButtonType
    data object Secondary : ButtonType
    data class DynamicColor(val containerColor: Color, val contentColor: Color) : ButtonType
}