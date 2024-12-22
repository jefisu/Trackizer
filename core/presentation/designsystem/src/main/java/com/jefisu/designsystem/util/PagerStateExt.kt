package com.jefisu.designsystem.util

import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable

@Composable
fun rememberEndlessPagerState(startPage: Int = 0) = rememberPagerState(
    pageCount = { Int.MAX_VALUE },
    initialPage = startPage,
)

fun <T> List<T>.getEndlessItem(index: Int) = this[index % size]

fun PagerState.calculateCurrentOffsetForPage(page: Int): Float =
    (currentPage - page) + currentPageOffsetFraction