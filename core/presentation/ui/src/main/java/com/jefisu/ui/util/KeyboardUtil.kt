@file:OptIn(ExperimentalLayoutApi::class)

package com.jefisu.ui.util

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun keyboardAsState(): State<Boolean> {
    val isVisbible = WindowInsets.isImeVisible
    return rememberUpdatedState(isVisbible)
}

@Composable
fun ObserveKeyboardVisibility(block: (Boolean) -> Unit) {
    val state = keyboardAsState()
    LaunchedEffect(Unit) {
        snapshotFlow { state.value }
            .distinctUntilChanged()
            .collectLatest {
                block(it)
            }
    }
}