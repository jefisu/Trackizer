package com.jefisu.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.AccentPrimary100

@Composable
fun <T> DataChangeIndicator(currentData: T) {
    val initialData = remember { currentData }
    AnimatedVisibility(
        visible = currentData != initialData,
    ) {
        Canvas(Modifier.size(4.dp)) {
            drawCircle(color = AccentPrimary100)
        }
    }
}