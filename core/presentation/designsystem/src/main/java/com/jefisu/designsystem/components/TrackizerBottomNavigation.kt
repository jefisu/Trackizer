package com.jefisu.designsystem.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Scaffold
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.FabIconColor
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.util.asDestinationRes
import com.jefisu.designsystem.util.dropShadow
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.ui.navigation.Destination
import com.jefisu.ui.screen.LocalScreenIsSmall

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrackizerBottomNavigation(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    selectedDestination: Destination = Destination.HomeScreen,
    onNavigateClick: (Destination) -> Unit = {},
    content: @Composable () -> Unit,
) {
    val isSmallScreen = LocalScreenIsSmall.current
    val bottomNavigation = @Composable {
        val bottomDestinations = listOf(
            Destination.HomeScreen,
            Destination.SpendingBudgetsScreen,
            Destination.AddSubscriptionScreen(id = null),
            Destination.CalendarScreen,
            Destination.CreditCardScreen,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        (if (isSmallScreen) 0.15f else 0.35f) to Gray80,
                    ),
                ),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .navigationBarsPadding()
                    .padding(horizontal = TrackizerTheme.spacing.extraMedium)
                    .padding(bottom = TrackizerTheme.spacing.extraSmall)
                    .paint(
                        painter = painterResource(R.drawable.bg_bottom_navigation),
                        contentScale = ContentScale.FillWidth,
                    ),
            ) {
                bottomDestinations.fastForEach { destination ->
                    if (destination is Destination.AddSubscriptionScreen) {
                        FabIcon(
                            painter = painterResource(destination.asDestinationRes()),
                            onClick = { onNavigateClick(destination) },
                            modifier = Modifier.offset(y = -TrackizerTheme.spacing.medium),
                        )
                    } else {
                        BottomNavItem(
                            icon = painterResource(destination.asDestinationRes()),
                            isSelected = selectedDestination == destination,
                            onClick = { onNavigateClick(destination) },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                bottomNavigation()
            }
        },
        content = { content() },
    )
}

@Composable
private fun FabIcon(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerSize: Dp = 66.dp,
) {
    fun DrawScope.drawBackground() {
        drawCircle(color = AccentPrimary100)
    }

    fun DrawScope.drawInnerGradient() {
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.43f to Color.Transparent,
                    1f to FabIconColor.copy(0.5f),
                ),
            ),
        )
    }

    fun DrawScope.drawStroke() {
        drawCircle(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.White.copy(0.5f),
                    1f to Color.Transparent,
                ),
                end = Offset(x = center.x, y = size.height),
            ),
            style = Stroke(width = 2.dp.toPx()),
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(containerSize),
    ) {
        Box(
            modifier = Modifier
                .dropShadow(
                    shape = CircleShape,
                    color = AccentPrimary100.copy(0.5f),
                    offsetY = 8.dp,
                    blur = 25.dp,
                )
                .size(containerSize - 10.dp)
                .drawWithContent {
                    drawBackground()
                    drawInnerGradient()
                    drawStroke()
                    drawContent()
                }
                .paint(painter)
                .clip(CircleShape)
                .rippleClickable {
                    onClick()
                },
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: Painter,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) Color.White else Gray30,
        label = "color anim",
    )

    Box(
        modifier
            .paint(
                painter = icon,
                colorFilter = ColorFilter.tint(color),
            )
            .rippleClickable(
                indication = rememberRipple(
                    bounded = false,
                    radius = 24.dp,
                ),
                onClick = onClick,
            )
            .minimumInteractiveComponentSize(),
    )
}

@Preview
@Composable
private fun TrackizerBottomNavigationPreview() {
    TrackizerTheme {
        TrackizerBottomNavigation(content = {})
    }
}
