@file:OptIn(ExperimentalFoundationApi::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import java.time.Month
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@Composable
fun <T> TrackizerPicker(
    items: List<T>,
    modifier: Modifier = Modifier,
    state: TrackizerPickerState = rememberTrackizerPickerState(itemsCount = items.size),
    visibleItemsCount: Int = 3,
    itemContent: @Composable (T) -> Unit,
) {
    val visibleItemsMiddle = visibleItemsCount / 2
    val listScrollCount = Int.MAX_VALUE
    val listScrollMiddle = listScrollCount / 2
    val listStartIndex =
        listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + state.selectedIndex

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    var itemHeightDp by remember { mutableStateOf(0.dp) }
    val fadingEdgeGradient = Brush.verticalGradient(
        0f to Color.Transparent,
        0.5f to Color.Black,
        1f to Color.Transparent,
    )

    fun getItem(index: Int) = items[index % items.size]

    LaunchedEffect(Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { it + visibleItemsMiddle }
            .distinctUntilChanged()
            .collect { state.setIndex(it) }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient),
        ) {
            items(listScrollCount) { index ->
                Box(
                    modifier = Modifier
                        .graphicsLayer { itemHeightDp = size.height.toDp() }
                        .padding(TrackizerTheme.spacing.extraSmall),
                ) {
                    itemContent(getItem(index))
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(top = itemHeightDp * visibleItemsMiddle)
                .height(itemHeightDp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(0.1f)),
        )
    }
}

@Composable
fun rememberTrackizerPickerState(
    itemsCount: Int,
    startIndex: Int = 0,
) = rememberSaveable(
    saver = TrackizerPickerState.Saver(),
) { TrackizerPickerState(startIndex, itemsCount) }

class TrackizerPickerState(private val startIndex: Int, private val count: Int) {

    var selectedIndex by mutableIntStateOf(startIndex)
        private set

    internal fun setIndex(index: Int) {
        selectedIndex = index % count
    }

    companion object {
        internal fun Saver(): Saver<TrackizerPickerState, Any> = listSaver(
            save = {
                listOf(
                    it.selectedIndex,
                    it.count,
                )
            },
            restore = {
                TrackizerPickerState(
                    startIndex = it[0] as Int,
                    count = it[1] as Int,
                )
            },
        )
    }
}

object TrackizerPickerDefaults {

    @Composable
    fun PickerItem(
        text: String,
        modifier: Modifier = Modifier,
        textStyle: TextStyle = TrackizerTheme.typography.headline4,
        leadingIcon: (@Composable RowScope.() -> Unit)? = null,
    ) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = TrackizerTheme.spacing.small,
                alignment = Alignment.CenterHorizontally,
            ),
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides textStyle,
            ) {
                leadingIcon?.invoke(this)
            }
            Text(
                text = text,
                maxLines = 1,
                style = textStyle,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Preview
@Composable
fun NumberPickerDemo() {
    val months = Month.entries.map { it.name }
    TrackizerTheme {
        TrackizerPicker(
            items = months,
            visibleItemsCount = 5,
            itemContent = { text ->
                TrackizerPickerDefaults.PickerItem(text)
            },
        )
    }
}