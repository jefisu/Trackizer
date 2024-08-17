package com.jefisu.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.dropShadow

@Composable
fun TrackizerButton(
    text: String,
    onClick: () -> Unit,
    type: ButtonType,
    modifier: Modifier = Modifier,
) {
    val shadowModifier = if (type.hasShadow) {
        Modifier.dropShadow(
            shape = CircleShape,
            color = type.containerColor.copy(alpha = 0.5f),
            blur = 25.dp,
            offsetY = 8.dp,
        )
    } else {
        Modifier
    }

    Button(
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            brush = type.borderGradient,
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = type.containerColor,
            contentColor = type.contentColor,
        ),
        modifier = modifier
            .widthIn(max = TrackizerTheme.size.buttonMaxWidth)
            .height(TrackizerTheme.size.buttonHeight)
            .then(shadowModifier),
    ) {
        Text(
            text = text,
            style = TrackizerTheme.typography.headline2,
        )
    }
}

sealed class ButtonType(
    val containerColor: Color,
    val hasShadow: Boolean = false,
    val contentColor: Color = Color.White,
    val borderGradient: Brush = SolidColor(Color.Transparent),
) {
    data object Primary : ButtonType(
        containerColor = AccentPrimary100,
        borderGradient = Brush.sweepGradient(
            0f to Color.White.copy(0.1f),
            0.8f to Color.White.copy(0.3f),
            1f to Color.Transparent,
        ),
        hasShadow = true,
    )

    data object Secondary : ButtonType(
        containerColor = Color.White.copy(0.1f),
        borderGradient = Brush.sweepGradient(
            0f to Color.White.copy(0.01f),
            0.8f to Color.White.copy(0.1f),
            1f to Color.Transparent,
        ),
    )
}

private class ButtonPreviewParamater : PreviewParameterProvider<ButtonType> {
    override val values get() = sequenceOf(ButtonType.Primary, ButtonType.Secondary)
}

@Preview
@Composable
private fun TrackizerButtonPreview(
    @PreviewParameter(ButtonPreviewParamater::class) type: ButtonType,
) {
    TrackizerTheme {
        TrackizerButton(
            text = "Get Started",
            onClick = {},
            type = type,
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        )
    }
}
