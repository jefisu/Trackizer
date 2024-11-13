@file:OptIn(ExperimentalLayoutApi::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.jefisu.designsystem.BorderBrush
import com.jefisu.designsystem.Gray20
import com.jefisu.designsystem.Gray40
import com.jefisu.designsystem.Gray60
import com.jefisu.designsystem.Gray70
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.CurrencyVisualTransformation
import com.jefisu.designsystem.util.LocalSettings
import com.jefisu.designsystem.util.rippleClickable

@Composable
fun CurrencyTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    maxLength: Int = 8,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
) {
    val settings = LocalSettings.current
    val focusManager = LocalFocusManager.current
    val isVisibleKeyboard = WindowInsets.isImeVisible
    val iconTint = Gray60

    fun updatePrice(value: Int) {
        val newValue = (text.toIntOrNull() ?: 0) + value
        if (newValue > -1) onTextChange(newValue.toString())
    }

    LaunchedEffect(isVisibleKeyboard) {
        if (!isVisibleKeyboard) focusManager.clearFocus()
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Rounded.Remove,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .roundedSquare()
                .rippleClickable {
                    updatePrice(-1)
                },
        )
        BasicTextField(
            value = text,
            onValueChange = { newValue ->
                if (newValue.isDigitsOnly() && newValue.length <= maxLength) {
                    newValue
                        .ifEmpty { "0" }
                        .let { if (it.length > 1) it.removePrefix("0") else it }
                        .let(onTextChange)
                }
            },
            modifier = Modifier.weight(1f),
            singleLine = true,
            visualTransformation = CurrencyVisualTransformation(
                settings.currency.toLocale(),
            ),
            cursorBrush = SolidColor(Color.White),
            textStyle = TrackizerTheme.typography.headline5.copy(
                textAlign = TextAlign.Center,
                color = Color.White,
            ),
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            decorationBox = { textFieldContent ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    label?.let {
                        Text(
                            text = it,
                            style = TrackizerTheme.typography.headline1,
                            color = Gray40,
                        )
                    }
                    textFieldContent()
                    HorizontalDivider(
                        color = Gray70,
                        modifier = Modifier.width(162.dp),
                    )
                }
            },
        )
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier
                .roundedSquare()
                .rippleClickable {
                    updatePrice(+1)
                },
        )
    }
}

fun String.toDecimalValue(): Float {
    if (this.isDigitsOnly() && this.isNotBlank()) {
        return this.toFloat() / 100f
    }

    return 0f
}

private fun Modifier.roundedSquare() = this
    .size(48.dp)
    .clip(RoundedCornerShape(16.dp))
    .background(Gray20.copy(0.05f))
    .border(
        width = 1.dp,
        shape = RoundedCornerShape(16.dp),
        brush = BorderBrush,
    )

@Preview
@Composable
private fun PriceInputPreview() {
    var text by remember { mutableStateOf("") }

    TrackizerTheme {
        CurrencyTextField(
            text = text,
            onTextChange = { text = it },
            label = "Montly Price",
            modifier = Modifier.padding(TrackizerTheme.spacing.medium),
        )
    }
}
