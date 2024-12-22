package com.jefisu.settings.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.jefisu.ui.screen.LocalScreenIsSmall

internal data class SettingOption(
    @DrawableRes val icon: Int,
    val title: String,
    val settingSelected: String? = null,
    val onClick: (() -> Unit)? = null,
    val leadingContent: @Composable (() -> Unit)? = null,
)

@Composable
internal fun SettingOptions(
    title: String,
    options: List<SettingOption>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = TrackizerTheme.typography.headline2,
        )
        Spacer(Modifier.height(TrackizerTheme.spacing.small))
        BoxWithConstraints {
            LazyColumn(
                userScrollEnabled = false,
                modifier = Modifier
                    .heightIn(max = maxHeight)
                    .background(Gray60.copy(0.2f), Shapes().large)
                    .border(
                        width = 1.dp,
                        brush = BorderBrush,
                        shape = Shapes().large,
                    ),
            ) {
                itemsIndexed(
                    items = options,
                    key = { index, _ -> index },
                ) { index, settingOption ->
                    val calculatePadding = @Composable { isDefaultPadding: Boolean ->
                        val defaultPadding = if (LocalScreenIsSmall.current) {
                            TrackizerTheme.spacing.medium
                        } else {
                            20.dp
                        }
                        when {
                            options.size == 1 -> defaultPadding
                            isDefaultPadding -> defaultPadding
                            else -> defaultPadding * 0.65f
                        }
                    }
                    SettingOptionItem(
                        option = settingOption,
                        modifier = Modifier
                            .rippleClickable(
                                enabled = settingOption.onClick != null,
                                onClick = { settingOption.onClick?.invoke() },
                            )
                            .padding(
                                start = TrackizerTheme.spacing.extraMedium,
                                end = TrackizerTheme.spacing.extraMedium,
                                top = calculatePadding(index == 0),
                                bottom = calculatePadding(index == options.lastIndex),
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingOptionItem(
    option: SettingOption,
    modifier: Modifier = Modifier,
) = with(option) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
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
            title = stringResource(R.string.general),
            options = listOf(
                SettingOption(
                    icon = R.drawable.ic_cloud_sync,
                    title = stringResource(R.string.cloud_sync),
                    leadingContent = {
                        TrackizerSwitch(
                            checked = false,
                            onCheckedChange = {},
                        )
                    },
                ),
            ),
            modifier = Modifier.padding(TrackizerTheme.spacing.small),
        )
    }
}