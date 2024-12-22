package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography

@Composable
fun TrackizerOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cornerDp: Dp = 16.dp,
    backgroundColor: Color = Gray80,
    contentColor: Color = Gray30,
    contentPadding: PaddingValues = PaddingValues(TrackizerTheme.spacing.extraSmall),
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(cornerDp),
        border = null,
        contentPadding = contentPadding,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
        ),
        modifier = modifier
            .fillMaxWidth()
            .dashBorder(cornerDp),
    ) {
        Text(
            text = text,
            style = TrackizerTheme.typography.headline2,
            color = contentColor,
        )
        Spacer(modifier = Modifier.width(TrackizerTheme.spacing.small))
        Icon(
            imageVector = Icons.Rounded.AddCircleOutline,
            contentDescription = text,
            tint = contentColor,
        )
    }
}

private fun Modifier.dashBorder(cornerDp: Dp) = drawBehind {
    drawRoundRect(
        color = Gray60,
        cornerRadius = CornerRadius(cornerDp.toPx()),
        style = Stroke(
            width = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
        ),
    )
}

@Preview
@Composable
private fun TrackizerOutlinedButtonPreview() {
    TrackizerTheme {
        TrackizerOutlinedButton(
            text = "Add",
            onClick = {},
            modifier = Modifier.padding(TrackizerTheme.spacing.medium),
        )
    }
}