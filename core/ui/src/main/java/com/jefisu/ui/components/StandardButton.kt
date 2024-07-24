package com.jefisu.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.jefisu.ui.modifier.dropShadow
import com.jefisu.ui.theme.AccentPrimary100
import com.jefisu.ui.theme.Theme

@Composable
fun StandardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    properties: ButtonProperties = ButtonProperties(),
) {
    val containerColor = when (properties.type) {
        is ButtonType.Primary -> AccentPrimary100
        is ButtonType.Secondary -> Color.White.copy(0.1f)
        is ButtonType.DynamicColor -> properties.type.containerColor
    }
    val contentColor = if (properties.type is ButtonType.DynamicColor) {
        properties.type.contentColor
    } else {
        LocalContentColor.current
    }

    val thenModifier = if (properties.type is ButtonType.Secondary) {
        Modifier
    } else {
        Modifier.dropShadow(
            shape = CircleShape,
            color = containerColor.copy(alpha = 0.5f),
            blur = 25.dp,
            offsetX = 8.dp,
        )
    }

    val gradientColors = if (properties.type is ButtonType.Primary) {
        listOf(
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
    } else {
        listOf(
            Color.White.copy(0.01f),
            Color.White.copy(0.1f),
            Color.White.copy(0f),
        )
    }

    val contentPadding = when (properties.size) {
        ButtonSize.Large -> {
            PaddingValues(
                horizontal = 24.dp,
                vertical = 14.dp
            )
        }

        ButtonSize.Small -> {
            PaddingValues(
                horizontal = 12.dp,
                vertical = 10.dp
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(max = 48.dp)
            .then(thenModifier)
            .clip(properties.shape)
            .background(containerColor)
            .clickable(
                enabled = properties.enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onClick() }
            .border(
                width = 1.dp,
                brush = Brush.sweepGradient(gradientColors),
                shape = properties.shape
            )
    ) {
        Row(
            modifier = Modifier
                .padding(contentPadding)
                .graphicsLayer {
                    alpha = if (properties.isLoading) 0f else 1f
                }
        ) {
            properties.leadingIconRes?.let { resId ->
                Icon(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier
                        .size(Theme.size.icon.extraSmall)
                        .align(Alignment.CenterVertically),
                )
                Spacer(modifier = Modifier.width(Theme.spacing.small))
            }
            Text(
                text = text,
                style = Theme.typography.headline2,
                color = contentColor
            )
        }

        CircularProgressIndicator(
            strokeCap = StrokeCap.Round,
            color = contentColor,
            modifier = Modifier
                .size(22.dp)
                .graphicsLayer {
                    alpha = if (properties.isLoading) 1f else 0f
                }
        )
    }
}

data class ButtonProperties(
    val type: ButtonType = ButtonType.Primary,
    val size: ButtonSize = ButtonSize.Large,
    val enabled: Boolean = true,
    @DrawableRes val leadingIconRes: Int? = null,
    val shape: Shape = CircleShape,
    val isLoading: Boolean = false,
)

sealed interface ButtonType {
    data object Primary : ButtonType
    data object Secondary : ButtonType
    data class DynamicColor(val containerColor: Color, val contentColor: Color) : ButtonType
}

sealed interface ButtonSize {
    data object Small : ButtonSize
    data object Large : ButtonSize
}