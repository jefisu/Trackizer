package com.jefisu.authentication.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.ui.theme.AccentSecondary100
import com.jefisu.ui.theme.Gray70
import com.jefisu.ui.theme.Gray80
import com.jefisu.ui.theme.Theme

enum class DifficultPassword {
    VULNERABLE,
    WEAK,
    STRONG,
    VERY_STRONG,
}

@Composable
fun MeasureDificultyPassword(
    modifier: Modifier = Modifier,
    difficultPassword: DifficultPassword? = null,
    thickness: Dp = 5.dp
) {
    val shapes = Shapes().small
    val selectedIndex = DifficultPassword.entries.indexOf(difficultPassword)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.small)
    ) {
        DifficultPassword.entries.forEachIndexed { i, dificulty ->
            val fillBackgroundAnim = remember { Animatable(0f) }
            LaunchedEffect(key1 = difficultPassword) {
                val nextOrBack = if (i <= selectedIndex) 1 else -1
                fillBackgroundAnim.animateTo(
                    targetValue = if (i <= selectedIndex) 1f else 0f,
                    animationSpec = tween(
                        durationMillis = 600,
                        delayMillis = 450 * ((i * nextOrBack) + 1)
                    )
                )
            }

            Box(
                modifier = Modifier
                    .height(thickness)
                    .weight(1f)
                    .clip(
                        shape = when (dificulty) {
                            DifficultPassword.VULNERABLE -> {
                                shapes.copy(
                                    topEnd = ZeroCornerSize,
                                    bottomEnd = ZeroCornerSize
                                )
                            }

                            DifficultPassword.VERY_STRONG -> {
                                shapes.copy(
                                    topStart = ZeroCornerSize,
                                    bottomStart = ZeroCornerSize
                                )
                            }

                            else -> RectangleShape
                        }
                    )
                    .background(Gray70)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            color = AccentSecondary100,
                            size = size.copy(
                                width = size.width * fillBackgroundAnim.value
                            )
                        )
                    }
            )
        }
    }
}

@Preview
@Composable
private fun MeasureDificultyPasswordPreview() {
    var difficultPasswordSelected by remember {
        mutableStateOf<DifficultPassword?>(null)
    }

    MeasureDificultyPassword(
        difficultPassword = difficultPasswordSelected,
        modifier = Modifier
            .width(300.dp)
            .background(Gray80)
            .padding(Theme.spacing.small)
            .clickable {
                if (difficultPasswordSelected == null) {
                    difficultPasswordSelected = DifficultPassword.WEAK
                    return@clickable
                }
                difficultPasswordSelected = null
            }
    )
}