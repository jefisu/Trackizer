package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.absoluteValue

@Composable
fun <T> CubeOutRotationEndlessTransition(
    modifier: Modifier = Modifier,
    items: List<T>,
    onItemVisibleChanged: (T) -> Unit,
    content: @Composable (T) -> Unit,
) {
    val pagerState = rememberPagerState { Int.MAX_VALUE }
    val enabledScroll by remember(items) {
        derivedStateOf { items.size > 1 }
    }

    fun getItem(index: Int) = items[index % items.size]

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            onItemVisibleChanged(getItem(index))
        }
        snapshotFlow { items.firstOrNull() }.collect { item ->
            val selectedItem = getItem(pagerState.currentPage)
            if (item != selectedItem) {
                onItemVisibleChanged(selectedItem)
            }
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = enabledScroll,
    ) { pageIndex ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .cubeOutScalingTransition(pageIndex, pagerState)
                .fillMaxWidth(),
        ) {
            content(getItem(pageIndex))
        }
    }
}

private fun Modifier.cubeOutScalingTransition(
    page: Int,
    pagerState: PagerState,
) = graphicsLayer {
    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
    when {
        pageOffset < -1f -> alpha = 0f
        pageOffset <= 0 -> {
            alpha = 1f
            transformOrigin = TransformOrigin(0f, 0.5f)
            rotationY = 90f * pageOffset.absoluteValue
        }

        pageOffset <= 1 -> {
            alpha = 1f
            transformOrigin = TransformOrigin(1f, 0.5f)
            rotationY = -90f * pageOffset.absoluteValue
        }

        else -> alpha = 0f
    }

    when {
        pageOffset.absoluteValue <= 0.5 -> {
            scaleY = 0.4f.coerceAtLeast(1 - pageOffset.absoluteValue)
            scaleX = 0.4f.coerceAtLeast(1 - pageOffset.absoluteValue)
        }

        pageOffset.absoluteValue <= 1 -> {
            scaleY = 0.4f.coerceAtLeast(pageOffset.absoluteValue)
            scaleX = 0.4f.coerceAtLeast(pageOffset.absoluteValue)
        }
    }
}

private fun PagerState.calculateCurrentOffsetForPage(page: Int): Float =
    (currentPage - page) + currentPageOffsetFraction