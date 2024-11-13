package com.jefisu.credit_cards.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.credit_cards.R
import com.jefisu.credit_cards.presentation.util.asFlagResource
import com.jefisu.designsystem.BorderBrush
import com.jefisu.designsystem.CreditCardColor
import com.jefisu.designsystem.Gray20
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.rippleClickable
import com.jefisu.domain.model.Card
import com.jefisu.ui.ext.formatExpirationDate
import com.jefisu.ui.util.SampleData

@Composable
fun CreditCard(
    card: Card,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: () -> Unit = {},
) {
    val cornerDp = 16.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(TrackizerTheme.size.creditCardWidth)
            .height(TrackizerTheme.size.creditCardHeight)
            .drawBehind {
                drawCardEdge(
                    rotateDegrees = 8f,
                    offset = Offset(24.dp.toPx(), -4.dp.toPx()),
                    cornerPx = cornerDp.toPx(),
                    alpha = 0.35f,
                )
                drawCardEdge(
                    rotateDegrees = 4f,
                    offset = Offset(16.dp.toPx(), -2.dp.toPx()),
                    cornerPx = cornerDp.toPx(),
                    alpha = 0.75f,
                )
                drawCardBackground(cornerDp.toPx())
            }
            .clip(RoundedCornerShape(cornerDp))
            .rippleClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .padding(TrackizerTheme.spacing.large),
    ) {
        Image(
            painter = painterResource(card.flag.asFlagResource()),
            contentDescription = card.name,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.width(56.dp),
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
        Text(
            text = card.name,
            style = TrackizerTheme.typography.headline3,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = card.cardHolder,
            style = TrackizerTheme.typography.headline1,
            color = Gray20,
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.small))
        Text(
            text = formatCardNumber(card.number),
            style = TrackizerTheme.typography.headline3,
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.small))
        Text(
            text = card.expirationDate.formatExpirationDate(),
            style = TrackizerTheme.typography.headline2,
        )
        Spacer(modifier = Modifier.height(TrackizerTheme.spacing.medium))
        Image(
            painter = painterResource(R.drawable.ic_chip),
            contentDescription = null,
        )
    }
}

private fun DrawScope.drawCardBackground(cornerPx: Float) {
    val cornerRadius = CornerRadius(cornerPx)
    val white5 = Color.White.copy(0.05f)

    val roundRectPath = Path().apply {
        addRoundRect(
            roundRect = RoundRect(
                rect = size.toRect(),
                cornerRadius = cornerRadius,
            ),
        )
        close()
    }

    clipPath(roundRectPath) {
        drawRoundRect(
            color = CreditCardColor,
            cornerRadius = cornerRadius,
        )
        drawStroke(cornerPx)
        translate(top = 125.dp.toPx()) {
            drawCircle(
                brush = Brush.linearGradient(
                    0f to white5,
                    0.6f to Color.Transparent,
                ),
                radius = size.maxDimension / 2,
            )
        }
        translate(
            left = 96.dp.toPx(),
            top = -101.dp.toPx(),
        ) {
            drawCircle(color = white5)
        }
    }
}

private fun DrawScope.drawCardEdge(
    rotateDegrees: Float,
    offset: Offset,
    cornerPx: Float,
    alpha: Float,
) {
    val cornerRadius = CornerRadius(cornerPx)
    withTransform(
        transformBlock = {
            rotate(
                degrees = rotateDegrees,
                pivot = center.copy(x = 0f),
            )
            translate(offset.x, offset.y)
        },
    ) {
        drawRoundRect(
            color = Gray60.copy(alpha),
            cornerRadius = cornerRadius,
        )
        drawRoundRect(
            brush = Brush.verticalGradient(
                listOf(
                    Color.Transparent,
                    Gray80.copy(0.75f),
                ),
            ),
            cornerRadius = cornerRadius,
        )
        drawStroke(cornerPx)
    }
}

private fun DrawScope.drawStroke(cornerPx: Float) {
    drawRoundRect(
        brush = BorderBrush,
        cornerRadius = CornerRadius(cornerPx),
        style = Stroke(width = 1.5.dp.toPx()),
    )
}

private fun formatCardNumber(number: String): String = "**** **** **** ${number.takeLast(4)}"

@Preview
@Composable
private fun CreditCardPreview() {
    TrackizerTheme {
        CreditCard(
            card = SampleData.cards.keys.first(),
            onClick = {},
            modifier = Modifier.padding(
                vertical = 40.dp,
                horizontal = 56.dp,
            ),
        )
    }
}
