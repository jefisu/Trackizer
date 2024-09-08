package com.jefisu.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Gray50
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.rippleClickable

@Composable
fun LabeledCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
        modifier = modifier,
    ) {
        RoundCheckbox(
            isChecked = isChecked,
            onCheckedChange = onCheckedChange,
        )
        label?.let {
            Text(
                text = it,
                style = TrackizerTheme.typography.bodyMedium,
                color = Gray50,
            )
        }
    }
}

@Composable
private fun RoundCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val size = TrackizerTheme.size.iconDefault
    val shape = Shapes().small

    val scaleCheckAnim by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        label = "",
        animationSpec = tween(300),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(shape)
            .rippleClickable {
                onCheckedChange(!isChecked)
            }
            .border(
                width = 1.dp,
                color = Gray70,
                shape = shape,
            )
            .graphicsLayer {
                scaleX = scaleCheckAnim
                scaleY = scaleX
            }
            .background(
                color = Gray60.copy(alpha = 0.2f),
                shape = shape,
            ),
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(0.8f),
        )
    }
}

@Preview
@Composable
private fun LabeledCheckboxPreview() {
    var isChecked by remember {
        mutableStateOf(false)
    }

    TrackizerTheme {
        LabeledCheckbox(
            isChecked = isChecked,
            onCheckedChange = { isChecked = it },
            label = "Remember me",
            modifier = Modifier
                .padding(TrackizerTheme.spacing.medium),
        )
    }
}
