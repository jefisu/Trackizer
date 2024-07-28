package com.jefisu.home.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.home.R
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray100
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Gray60
import com.jefisu.ui.theme.Purple90
import com.jefisu.ui.theme.Theme
import kotlinx.coroutines.launch

@Composable
internal fun HorizontalTabs(
    modifier: Modifier = Modifier,
    tabContent: (@Composable (SubscriptionTab) -> Unit)? = null,
) {
    val tabs = SubscriptionTab.entries
    val shapeSize = 16.dp

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { tabs.size }
    val selectedTabAnim = remember { Animatable(0f) }

    fun DrawScope.drawSelectedTab() {
        val cornerRadius = CornerRadius(shapeSize.toPx(), shapeSize.toPx())
        val tabWidth = size.width / tabs.size
        val size = size.copy(width = tabWidth)
        val offset = Offset(tabWidth * selectedTabAnim.value, 0f)

        drawRoundRect(
            brush = Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Purple90.copy(0.15f),
                    1f to Color.Transparent,
                ),
                start = offset,
                end = center.copy(y = size.height),
            ),
            topLeft = offset,
            size = size,
            cornerRadius = cornerRadius,
            style = Stroke(width = 1.dp.toPx()),
        )

        drawRoundRect(
            color = Gray60.copy(0.2f),
            topLeft = offset,
            size = size,
            cornerRadius = cornerRadius,
        )
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.medium),
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(shapeSize))
                .background(Gray100)
                .padding(Theme.spacing.small)
                .drawBehind { drawSelectedTab() },
        ) {
            tabs.forEach { tab ->
                TabItem(
                    title = stringResource(tab.titleId),
                    isSelected = tab.ordinal == selectedTabAnim.value.toInt(),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            launch {
                                selectedTabAnim.animateTo(
                                    targetValue = tab.ordinal.toFloat(),
                                    animationSpec = tween(500),
                                )
                            }
                            launch {
                                pagerState.animateScrollToPage(
                                    page = tab.ordinal,
                                    animationSpec = tween(500),
                                )
                            }
                        }
                    },
                )
            }
        }
        tabContent?.let { content ->
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) { page ->
                content(tabs[page])
            }
        }
    }
}

@Preview
@Composable
private fun HorizontalTabsPreview() {
    AppTheme {
        HorizontalTabs(
            modifier = Modifier.padding(Theme.spacing.small),
        )
    }
}

@Composable
private fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = Shapes().large,
) {
    val enabledColor = Color.White
    val disabledColor = Gray30

    val color by animateColorAsState(
        targetValue = if (isSelected) enabledColor else disabledColor,
        label = "title color",
    )

    Text(
        text = title,
        style = Theme.typography.headline1,
        color = color,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            ) { onClick() }
            .padding(12.dp),
    )
}

enum class SubscriptionTab(@StringRes val titleId: Int) {
    YOUR_SUBSCRIPTIONS(R.string.your_subscriptions_tab),
    UPCOMING_BILLS(R.string.upcoming_bills_tab),
}
