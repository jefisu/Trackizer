package com.jefisu.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.ui.R
import com.jefisu.ui.modifier.dropShadow
import com.jefisu.ui.theme.AccentPrimary100
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.FabIconColor
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Theme

@Composable
fun StandardBottomNavigation(
    onNavClick: (BottomNavItem) -> Unit,
    modifier: Modifier = Modifier,
    applyShadow: Boolean = true,
) {
    val bottomNavItems = BottomNavItem.entries
    var selectedNavItem by rememberSaveable { mutableIntStateOf(0) }

    val backgroundShadow = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to Color.Transparent,
            0.51f to Theme.backgroundColor,
        ),
    )

    Box(
        modifier = modifier
            .height(156.dp)
            .background(
                if (applyShadow) backgroundShadow else SolidColor(Color.Transparent),
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(
                    bottom = Theme.spacing.extraMedium,
                    start = Theme.spacing.extraMedium,
                    end = Theme.spacing.extraMedium,
                )
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .paint(
                    painter = painterResource(R.drawable.bg_bottom_navigation),
                    contentScale = ContentScale.FillWidth,
                ),
        ) {
            bottomNavItems.forEach { navItem ->
                val painter = painterResource(navItem.resId)
                if (navItem == BottomNavItem.ADD) {
                    FabIcon(
                        painter = painter,
                        onClick = { onNavClick(navItem) },
                        modifier = Modifier.offset(y = -Theme.spacing.medium),
                    )
                } else {
                    BottomNavItem(
                        icon = painter,
                        isSelected = selectedNavItem == navItem.ordinal,
                        onClick = {
//                            selectedNavItem = navItem.ordinal
                            onNavClick(navItem)
                        },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun StandardBottomNavBarPreview() {
    AppTheme {
        StandardBottomNavigation(
            onNavClick = { },
        )
    }
}

enum class BottomNavItem(@DrawableRes val resId: Int) {
    HOME(R.drawable.ic_home),
    BUDGETS(R.drawable.ic_budgets),
    ADD(R.drawable.ic_rounded_add),
    CALENDAR(R.drawable.ic_calendar),
    CREDIT_CARDS(R.drawable.ic_credit_cards),
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
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    onClick = onClick,
                ),
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

    Box(modifier) {
        Box(
            Modifier
                .paint(
                    painter = icon,
                    colorFilter = ColorFilter.tint(color),
                )
                .align(Alignment.Center)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(
                        bounded = false,
                        radius = 24.dp,
                    ),
                    onClick = onClick,
                )
                .minimumInteractiveComponentSize(),
        )
    }
}
