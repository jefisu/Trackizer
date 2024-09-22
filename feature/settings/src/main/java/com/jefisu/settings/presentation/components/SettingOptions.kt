package com.jefisu.settings.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.BorderBrush
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.TrackizerSwitch
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.settings.R

@Composable
internal fun SettingOptions(
    title: String,
    modifier: Modifier = Modifier,
    options: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = TrackizerTheme.typography.headline2,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.small))
        Column(
            verticalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.medium),
            modifier = Modifier
                .background(Gray60.copy(0.2f), Shapes().large)
                .border(
                    width = 1.dp,
                    brush = BorderBrush,
                    shape = Shapes().large,
                )
                .padding(
                    horizontal = TrackizerTheme.spacing.extraMedium,
                    vertical = TrackizerTheme.spacing.medium,
                ),
        ) {
            options()
        }
    }
}

@Composable
internal fun SettingOptionItem(
    @DrawableRes icon: Int,
    title: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    settingSelected: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(32.dp)
            .rippleClickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke() },
            ),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = title,
            modifier = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(TrackizerTheme.spacing.medium))
        Text(
            text = title,
            style = TrackizerTheme.typography.headline2,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(TrackizerTheme.spacing.medium))
        settingSelected?.let { text ->
            Text(
                text = text,
                style = TrackizerTheme.typography.bodySmall,
                color = Gray30,
            )
            Spacer(Modifier.width(TrackizerTheme.spacing.small))
            Icon(
                painter = painterResource(DesignSystemRes.drawable.ic_back),
                contentDescription = "",
                tint = Gray30,
                modifier = Modifier
                    .size(TrackizerTheme.size.iconSmall)
                    .rotate(180f),
            )
        } ?: leadingContent?.invoke()
    }
}

@Preview
@Composable
private fun SettingOptionsPreview() {
    TrackizerTheme {
        SettingOptions(
            title = "General",
            modifier = Modifier
                .padding(TrackizerTheme.spacing.small),
        ) {
            SettingOptionItem(
                icon = R.drawable.ic_cloud_sync,
                title = "Cloud Sync",
                leadingContent = {
                    TrackizerSwitch(
                        checked = false,
                        onCheckedChange = {},
                    )
                },
            )
        }
    }
}
