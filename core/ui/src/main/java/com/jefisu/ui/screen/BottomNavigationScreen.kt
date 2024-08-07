package com.jefisu.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.ui.R
import com.jefisu.ui.components.BottomNavItem
import com.jefisu.ui.components.BottomNavigation
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Theme

@Composable
fun BottomNavigationScreen(
    showBottomNavigation: Boolean = true,
    selectedNavItem: BottomNavItem = BottomNavItem.HOME,
    onNavItemClick: (BottomNavItem) -> Unit = {},
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        content()
        AnimatedVisibility(
            visible = showBottomNavigation,
            modifier = Modifier.align(Alignment.BottomStart),
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            BottomNavigation(
                onNavClick = onNavItemClick,
                selectedNavItem = selectedNavItem,
            )
        }
    }
}

@Composable
fun BottomNavigationBody(
    title: String? = null,
    onSettingsClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        content()
        title?.let { text ->
            Text(
                text = text,
                style = Theme.typography.bodyLarge,
                color = Gray30,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = Theme.spacing.large)
                    .statusBarsPadding(),
            )
        }
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(Theme.spacing.medium)
                .statusBarsPadding(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_settings),
                contentDescription = "Settings icon",
                tint = Gray30,
            )
        }
    }
}

@Preview
@Composable
private fun BottomNavigationScreenPreview() {
    AppTheme {
        BottomNavigationScreen {
            BottomNavigationBody(
                title = "Calendar",
                onSettingsClick = { },
                content = { },
            )
        }
    }
}
