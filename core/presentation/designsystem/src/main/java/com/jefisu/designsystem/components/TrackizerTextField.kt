@file:OptIn(ExperimentalFoundationApi::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.designsystem.util.TrackizerTextFieldDefaults

@Composable
fun TrackizerTextField(
    text: String,
    onTextChange: (String) -> Unit,
    fieldName: String,
    modifier: Modifier = Modifier,
    fieldNameStyle: TextStyle = TrackizerTheme.typography.bodyMedium,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }

    val textField: @Composable RowScope.() -> Unit = {
        BasicTextField(
            value = text,
            onValueChange = onTextChange,
            cursorBrush = SolidColor(LocalContentColor.current),
            singleLine = true,
            textStyle = LocalTextStyle.current,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .weight(1f)
                .onFocusChanged { isFocused = it.isFocused },
        )
        if (text.isNotEmpty()) {
            Spacer(Modifier.width(TrackizerTheme.spacing.small))
            IconButton(onClick = { onTextChange("") }) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Clear text",
                )
            }
        }
    }

    TrackizerTextFieldDefaults.DecorationBox(
        fieldName = fieldName,
        modifier = modifier,
        fieldNameStyle = fieldNameStyle,
        isFocused = isFocused,
        textField = textField,
    )
}

@Preview
@Composable
private fun TrackizerTextFieldPreview() {
    var text by rememberSaveable { mutableStateOf("") }

    TrackizerTheme {
        TrackizerTextField(
            text = text,
            onTextChange = { text = it },
            fieldName = "E-mail",
        )
    }
}
