package com.jefisu.subscription_info.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.DataChangeIndicator
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.subscription_info.presentation.util.InfoRow
import com.jefisu.subscription_info.presentation.util.InfoRowType
import com.jefisu.ui.R as UiRes

@Composable
fun InfoRowItem(
    info: InfoRow,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(32.dp),
    ) {
        DataChangeIndicator(
            currentData = info.value,
        )
        Text(
            text = stringResource(info.type.titleId),
            style = TrackizerTheme.typography.headline2,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = info.value.ifBlank { info.label.orEmpty() },
            style = TrackizerTheme.typography.bodySmall,
            color = Gray30,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.widthIn(max = 130.dp),
        )
        Icon(
            painter = painterResource(DesignSystemRes.drawable.ic_back),
            contentDescription = null,
            tint = Gray30,
            modifier = Modifier
                .size(TrackizerTheme.size.iconSmall)
                .rotate(180f),
        )
    }
}

@Preview
@Composable
private fun InfoRowItemPreview() {
    TrackizerTheme {
        InfoRowItem(
            info = InfoRow(
                value = "This is a sample description",
                label = stringResource(
                    id = UiRes.string.no_data,
                    stringResource(UiRes.string.description).lowercase(),
                ),
                type = InfoRowType.Description,
            ),
            modifier = Modifier
                .width(250.dp)
                .padding(TrackizerTheme.spacing.small),
        )
    }
}