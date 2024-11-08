package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import com.jefisu.designsystem.util.calculateCurrentOffsetForPage
import com.jefisu.designsystem.util.getEndlessItem
import com.jefisu.designsystem.util.rememberEndlessPagerState
import kotlin.math.absoluteValue

@Composable
fun <T> CubeOutRotationEndlessTransition(
    modifier: Modifier = Modifier,
    onItemVisibleChanged: ((T) -> Unit)? = null,
    items: List<T>,
    content: @Composable (T) -> Unit,
) {
    val pagerState = rememberEndlessPagerState()
    val enabledScroll by remember(items) {
        derivedStateOf { items.size > 1 }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            onItemVisibleChanged?.invoke(items.getEndlessItem(index))
        }
        snapshotFlow { items.firstOrNull() }.collect { item ->
            val selectedItem = items.getEndlessItem(pagerState.currentPage)
            if (item != selectedItem) {
                onItemVisibleChanged?.invoke(selectedItem)
            }
        }
    }

    HorizontalPager(
        modifier = modifier,
        state = pagerState,
        userScrollEnabled = enabledScroll,
    ) { pageIndex ->
        val item = items.getEndlessItem(pageIndex)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .cubeOutScalingTransition(pageIndex, pagerState)
                .fillMaxWidth(),
        ) {
            content(item)
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
