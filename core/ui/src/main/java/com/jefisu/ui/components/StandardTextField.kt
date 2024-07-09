package com.jefisu.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Theme

@Composable
fun StandardTextField(
    text: String,
    onTextChange: (String) -> Unit,
    fieldName: String,
    modifier: Modifier = Modifier,
    fieldNameAlign: TextAlign = TextAlign.Start,
    isPasswordField: Boolean = false,
    isEnabled: Boolean = true
) {
    var isFocused by rememberSaveable {
        mutableStateOf(false)
    }
    val color by animateColorAsState(
        targetValue = if (isFocused) Color.White else Gray50,
        label = "content color animated",
    )

    var showContent by rememberSaveable { mutableStateOf(false) }
    val iconPassword =
        if (showContent) {
            Icons.Rounded.VisibilityOff
        } else {
            Icons.Rounded.Visibility
        }

    val focusManager = LocalFocusManager.current
    val isVisibleKeyboard = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    LaunchedEffect(key1 = isVisibleKeyboard) {
        if (!isVisibleKeyboard) {
            focusManager.clearFocus()
        }
    }

    Column {
        Text(
            text = fieldName,
            style = Theme.typography.bodyMedium,
            color = color,
            fontWeight = FontWeight.Medium,
            textAlign = fieldNameAlign,
            modifier = modifier,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier =
                modifier
                    .widthIn(min = 250.dp)
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = color,
                        shape = Shapes().large,
                    ).padding(horizontal = Theme.spacing.medium),
        ) {
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                singleLine = true,
                enabled = isEnabled,
                cursorBrush = SolidColor(color),
                textStyle =
                    Theme.typography.bodyLarge.copy(
                        color = color,
                    ),
                visualTransformation =
                    if (!isPasswordField || showContent) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                modifier =
                    Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            isFocused = focusState.isFocused
                        },
            )
            if (text.isNotBlank() && isPasswordField) {
                Icon(
                    imageVector = iconPassword,
                    contentDescription = null,
                    tint = color,
                    modifier =
                        Modifier
                            .size(Theme.size.icon.extraMedium)
                            .clickable { showContent = !showContent },
                )
            }
        }
    }
}

@Preview
@Composable
private fun StandardTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    AppTheme {
        StandardTextField(
            text = text,
            onTextChange = { text = it },
            fieldName = "E-mail address",
        )
    }
}
