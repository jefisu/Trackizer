package com.jefisu.auth.presentation.auth_provider_pages.login.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray60
import com.jefisu.ui.theme.Gray70
import com.jefisu.ui.theme.Theme

@Composable
fun RoundCheckbox(
    isChecked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val size = Theme.size.icon.extraMedium
    val shape = Shapes().small

    val scaleCheckAnim by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        label = "",
        animationSpec = tween(300)
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(shape)
            .clickable {
                onCheckedChange()
            }
            .border(
                width = 1.dp,
                color = Gray70,
                shape = shape
            )
            .scale(scaleCheckAnim)
            .background(
                color = Gray60.copy(alpha = 0.2f),
                shape = shape
            )
    ) {
        Icon(
            imageVector = Icons.Rounded.Check,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.fillMaxSize(0.8f)
        )
    }
}

@Preview
@Composable
private fun RoundCheckboxPreview() {
    var isChecked by remember {
        mutableStateOf(false)
    }

    AppTheme {
        RoundCheckbox(
            isChecked = isChecked,
            onCheckedChange = { isChecked = !isChecked },
            modifier = Modifier
                .padding(Theme.spacing.extraMedium)
                .scale(2f)
        )
    }
}