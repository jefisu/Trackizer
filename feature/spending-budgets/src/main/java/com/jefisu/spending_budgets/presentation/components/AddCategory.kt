package com.jefisu.spending_budgets.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jefisu.spending_budgets.R
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Gray60
import com.jefisu.ui.theme.Theme

@Composable
fun AddCategory(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val corner = 16.dp
    val color = Gray30

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(corner))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
            ) { onClick() }
            .drawBehind {
                val cornerPx = corner.toPx()
                drawRoundRect(
                    color = Gray60,
                    cornerRadius = CornerRadius(cornerPx, cornerPx),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
                    ),
                )
            }
            .padding(Theme.spacing.large),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.add_new_category),
                style = Theme.typography.headline2,
                color = color,
            )
            Spacer(modifier = Modifier.width(Theme.spacing.small))
            Icon(
                imageVector = Icons.Rounded.AddCircleOutline,
                contentDescription = stringResource(R.string.add_new_category),
                tint = color,
            )
        }
    }
}
