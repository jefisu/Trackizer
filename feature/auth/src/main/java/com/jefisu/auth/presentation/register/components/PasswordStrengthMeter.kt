package com.jefisu.auth.presentation.register.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.auth.presentation.register.util.PasswordStrength
import com.jefisu.designsystem.AccentSecondary100
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import kotlinx.coroutines.delay

@Composable
internal fun PasswordStrengthMeter(
    modifier: Modifier = Modifier,
    passwordStrength: PasswordStrength? = null,
    thickness: Dp = 5.dp,
) {
    val shapes = Shapes().small
    var progress by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(key1 = passwordStrength) {
        passwordStrength?.let { value ->
            val selectedIndex = PasswordStrength.entries.indexOf(value)
            while (progress != selectedIndex) {
                progress += if (progress < selectedIndex) 1 else -1
                delay(300)
            }
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
    ) {
        PasswordStrength.entries.forEachIndexed { i, strength ->
            val widthAnim by animateFloatAsState(
                targetValue = if (i <= progress && passwordStrength != null) 1f else 0f,
                animationSpec = tween(durationMillis = 1000),
                label = "width",
            )

            Box(
                modifier = Modifier
                    .height(thickness)
                    .weight(1f)
                    .clip(
                        shape = when (strength) {
                            PasswordStrength.VULNERABLE -> {
                                shapes.copy(
                                    topEnd = ZeroCornerSize,
                                    bottomEnd = ZeroCornerSize,
                                )
                            }

                            PasswordStrength.VERY_STRONG -> {
                                shapes.copy(
                                    topStart = ZeroCornerSize,
                                    bottomStart = ZeroCornerSize,
                                )
                            }

                            else -> RectangleShape
                        },
                    )
                    .background(Gray70)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            color = AccentSecondary100,
                            size = size.copy(
                                width = size.width * widthAnim,
                            ),
                        )
                    },
            )
        }
    }
}

@Preview
@Composable
private fun PasswordStrengthMeterPreview() {
    var selectedPasswordStrength by remember {
        mutableStateOf<PasswordStrength?>(null)
    }

    PasswordStrengthMeter(
        passwordStrength = selectedPasswordStrength,
        modifier = Modifier
            .width(300.dp)
            .background(Gray80)
            .padding(TrackizerTheme.spacing.small)
            .clickable {
                if (selectedPasswordStrength == null) {
                    selectedPasswordStrength = PasswordStrength.WEAK
                    return@clickable
                }
                selectedPasswordStrength = null
            },
    )
}
