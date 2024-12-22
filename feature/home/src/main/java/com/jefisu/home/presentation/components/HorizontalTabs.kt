package com.jefisu.home.presentation.components

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Gray100
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Purple90
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.home.R
import com.jefisu.ui.screen.LocalScreenIsSmall
import kotlinx.coroutines.launch

@Composable
internal fun HorizontalTabs(
    modifier: Modifier = Modifier,
    shapeSize: Dp = 16.dp,
    tabContent: (@Composable (SubscriptionTab) -> Unit)? = null,
) {
    val tabs = SubscriptionTab.entries
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { tabs.size }
    val selectedTabAnim = rememberSeletableTab()
    val isSmallScreen = LocalScreenIsSmall.current

    Column(
        verticalArrangement = Arrangement.spacedBy(
            if (isSmallScreen) {
                TrackizerTheme.spacing.extraSmall
            } else {
                TrackizerTheme.spacing.medium
            },
        ),
    ) {
        Row(
            modifier = modifier
                .clip(RoundedCornerShape(shapeSize))
                .background(Gray100)
                .padding(TrackizerTheme.spacing.small)
                .selectedTab(
                    selectedIndex = { selectedTabAnim.value },
                    tabsCount = pagerState.pageCount,
                    cornerSize = shapeSize,
                ),
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
    TrackizerTheme {
        HorizontalTabs(
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
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
    val color by animateColorAsState(
        targetValue = if (isSelected) Color.White else Gray30,
        label = "title color",
    )

    Text(
        text = title,
        style = TrackizerTheme.typography.headline1,
        color = color,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clip(shape)
            .rippleClickable {
                onClick()
            }
            .padding(12.dp),
    )
}

private fun Modifier.selectedTab(
    selectedIndex: () -> Float,
    tabsCount: Int,
    cornerSize: Dp,
) = drawBehind {
    val cornerRadius = CornerRadius(cornerSize.toPx())
    val tabWidth = size.width / tabsCount
    val size = size.copy(width = tabWidth)
    val offset = Offset(tabWidth * selectedIndex(), 0f)

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

@Composable
fun rememberSeletableTab() = rememberSaveable(
    saver = Saver(
        save = { it.value },
        restore = { Animatable(it) },
    ),
) {
    Animatable(0f)
}

internal enum class SubscriptionTab(@StringRes val titleId: Int) {
    YOUR_SUBSCRIPTIONS(R.string.your_subscriptions_tab),
    UPCOMING_BILLS(R.string.upcoming_bills_tab),
}