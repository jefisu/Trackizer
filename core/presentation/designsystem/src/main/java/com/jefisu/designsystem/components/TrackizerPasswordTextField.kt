@file:OptIn(ExperimentalFoundationApi::class)

package com.jefisu.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.util.TrackizerTextFieldDefaults
import com.jefisu.designsystem.util.automaticallyClearFocus

@Composable
fun TrackizerPasswordTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    var isFocused by rememberSaveable { mutableStateOf(false) }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    val textField: @Composable RowScope.() -> Unit = {
        Box(modifier = Modifier.weight(1f)) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                cursorBrush = SolidColor(LocalContentColor.current),
                singleLine = true,
                textStyle = LocalTextStyle.current,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                visualTransformation = if (showPassword) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                modifier = Modifier
                    .automaticallyClearFocus()
                    .onFocusChanged { isFocused = it.isFocused },
            )
        }
        Spacer(Modifier.width(TrackizerTheme.spacing.small))
        if (text.isNotEmpty()) {
            IconButton(
                onClick = { onTextChange("") },
                modifier = Modifier.offset(x = TrackizerTheme.spacing.small),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Clear text",
                )
            }
        }
        IconButton(onClick = { showPassword = !showPassword }) {
            Icon(
                imageVector = if (showPassword) {
                    Icons.Rounded.VisibilityOff
                } else {
                    Icons.Rounded.Visibility
                },
                contentDescription = "Password visibility",
            )
        }
    }

    TrackizerTextFieldDefaults.DecorationBox(
        modifier = modifier,
        fieldName = stringResource(R.string.password),
        isFocused = isFocused,
        textField = textField,
    )
}

@Preview
@Composable
private fun TrackizerPasswordTextFieldPreview() {
    TrackizerTheme {
        TrackizerPasswordTextField(
            text = "",
            onTextChange = {},
        )
    }
}
