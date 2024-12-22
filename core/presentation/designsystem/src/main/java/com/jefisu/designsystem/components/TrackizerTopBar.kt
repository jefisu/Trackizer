@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.typography

@Composable
fun TrackizerTopBar(
    title: String?,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    colors: TopAppBarColors = TrackizerTopBarDefaults.colors,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title.orEmpty(),
                style = TrackizerTheme.typography.bodyLarge,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = colors,
        scrollBehavior = scrollBehavior,
        modifier = modifier,
    )
}

object TrackizerTopBarDefaults {

    val colors: TopAppBarColors
        @Composable
        get() = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = Gray30,
            actionIconContentColor = Gray30,
            navigationIconContentColor = Gray30,
        )

    @Composable
    fun settingsActionIcon(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings icon",
            )
        }
    }

    @Composable
    fun backNavigationIcon(
        modifier: Modifier = Modifier,
        onClick: () -> Unit,
    ) {
        IconButton(
            onClick = onClick,
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back navigation icon",
            )
        }
    }
}