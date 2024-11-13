package com.jefisu.auth.presentation.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jefisu.designsystem.util.getEndlessItem
import com.jefisu.designsystem.util.rememberEndlessPagerState
import kotlinx.coroutines.launch

@Composable
fun <T> EndlessHorizontalPager(
    modifier: Modifier = Modifier,
    startPage: Int = 0,
    items: List<T>,
    pageContent: @Composable (T, () -> Unit) -> Unit,
) {
    val pagerState = rememberEndlessPagerState(
        startPage = startPage,
    )
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        userScrollEnabled = false,
        verticalAlignment = Alignment.Bottom,
    ) { pageIndex ->
        pageContent(
            items.getEndlessItem(pageIndex),
        ) {
            scope.launch {
                pagerState.animateScrollToPage(
                    page = pageIndex + 1,
                    animationSpec = tween(500),
                )
            }
        }
    }
}
