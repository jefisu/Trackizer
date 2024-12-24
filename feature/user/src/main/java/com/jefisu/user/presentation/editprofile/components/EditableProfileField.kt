@file:OptIn(ExperimentalLayoutApi::class)

package com.jefisu.user.presentation.editprofile.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import com.jefisu.designsystem.AccentPrimary100
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.Gray40
import com.jefisu.designsystem.R as DesignSystemRes
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.DataChangeIndicator
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.util.ObserveKeyboardVisibility

@Composable
fun EditableProfileField(
    title: String,
    textFieldValue: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    color: Color = LocalContentColor.current,
    textStyle: TextStyle = LocalTextStyle.current.copy(color = color),
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    var isFocused by rememberSaveable { mutableStateOf(false) }
    val spacing = contentPadding.calculateStartPadding(LayoutDirection.Rtl)
    val titleColorAnim by animateColorAsState(
        targetValue = if (isFocused) AccentPrimary100 else color,
        label = "titleColorAnim",
    )

    ObserveKeyboardVisibility { isVisible ->
        if (!isVisible) focusManager.clearFocus()
    }

    val unfocusedContent = @Composable {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(end = spacing),
        ) {
            Text(
                text = textFieldValue.text.ifBlank { stringResource(UiRes.string.no_data) },
                textAlign = TextAlign.Right,
                color = Gray40,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(TrackizerTheme.spacing.small))
            leadingIcon?.invoke() ?: run {
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
    }

    BasicTextField(
        value = textFieldValue,
        onValueChange = onTextChange,
        textStyle = textStyle.copy(
            textAlign = TextAlign.Right,
        ),
        cursorBrush = SolidColor(color),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        modifier = Modifier
            .height(TrackizerTheme.size.editableProfileFieldHeight)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        decorationBox = { textField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(TrackizerTheme.spacing.small),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = spacing),
            ) {
                DataChangeIndicator(
                    currentData = textFieldValue.text,
                )
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColorAnim,
                )
                AnimatedContent(
                    targetState = isFocused,
                    modifier = Modifier.weight(1f),
                    transitionSpec = {
                        slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(700),
                        ) togetherWith slideOutOfContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(700),
                        )
                    },
                ) { hasFocus ->
                    if (hasFocus) {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.height(IntrinsicSize.Min),
                        ) {
                            VerticalDivider()
                            Spacer(
                                Modifier
                                    .weight(1f)
                                    .padding(end = TrackizerTheme.spacing.small),
                            )
                            textField()
                            Spacer(Modifier.width(spacing))
                        }
                    } else {
                        unfocusedContent()
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun EditableProfileFieldPreview() {
    var textFieldValue by remember { mutableStateOf(TextFieldValue("John Dow")) }
    TrackizerTheme {
        EditableProfileField(
            title = "Name",
            textFieldValue = textFieldValue,
            onTextChange = { textFieldValue = it },
            contentPadding = PaddingValues(
                horizontal = TrackizerTheme.spacing.extraMedium,
            ),
        )
    }
}