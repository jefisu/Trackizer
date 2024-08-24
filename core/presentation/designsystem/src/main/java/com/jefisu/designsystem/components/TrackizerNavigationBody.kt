@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.typography

@Composable
fun TrackizerNavigationBody(
    title: String?,
    onSettingsClick: () -> Unit,
    topBarContainerColor: Color = Color.Transparent,
    scrolledContainerColor: Color = Color.Transparent,
    topAppBarScrollBehavior: TopAppBarScrollBehavior? = null,
    actionIcon: @Composable RowScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val backgroundColor = Color.Transparent
    val scrollBehaviorModifier = topAppBarScrollBehavior?.let { scrollBehavior ->
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    } ?: Modifier

    val topBar = @Composable {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = title.orEmpty(),
                    style = TrackizerTheme.typography.bodyLarge,
                )
            },
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_settings),
                        contentDescription = "Settings icon",
                    )
                }
                actionIcon()
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = topBarContainerColor,
                scrolledContainerColor = scrolledContainerColor,
                titleContentColor = Gray30,
                actionIconContentColor = Gray30,
            ),
            scrollBehavior = topAppBarScrollBehavior,
        )
    }

    Scaffold(
        content = content,
        containerColor = backgroundColor,
        topBar = topBar,
        modifier = Modifier
            .then(scrollBehaviorModifier),
    )
}
