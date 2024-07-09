package com.jefisu.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.jefisu.common.util.MessageText
import com.jefisu.common.util.UiText
import com.jefisu.ui.R
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.ErrorContainerColor
import com.jefisu.ui.theme.HelpContainerColor
import com.jefisu.ui.theme.SuccessContainerColor
import com.jefisu.ui.theme.Theme
import com.jefisu.ui.theme.WarningContainerColor

@Composable
fun FlashMessageContainer(
    modifier: Modifier = Modifier,
    message: MessageText?,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    var isVisible by rememberSaveable(message) {
        mutableStateOf(message != null)
    }

    if (isVisible) {
        val flashMessageType = when (message) {
            is MessageText.Error -> FlashMessageType.ERROR
            is MessageText.Warning -> FlashMessageType.WARNING
            is MessageText.Help -> FlashMessageType.HELP
            else -> FlashMessageType.SUCCESS
        }

        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(modifier = modifier.fillMaxSize()) {
                FlashMessage(
                    message = message?.text?.asString().orEmpty(),
                    type = flashMessageType,
                    onCloseClick = {
                        isVisible = false
                        onDismiss()
                    },
                    modifier = Modifier
                        .safeDrawingPadding()
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(Theme.spacing.medium)
                )
            }
        }
    }
    content()
}

@Composable
internal fun FlashMessage(
    modifier: Modifier = Modifier,
    message: String,
    type: FlashMessageType,
    onCloseClick: () -> Unit = {}
) {
    val shape = RoundedCornerShape(32.dp)

    Box(
        modifier = modifier.clip(shape)
    ) {
        Image(
            painter = painterResource(id = type.iconId),
            contentDescription = null,
            modifier = Modifier
                .zIndex(1f)
                .padding(start = 20.dp)
                .size(58.dp)
        )
        Image(
            painter = painterResource(id = type.backgroundIconId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .zIndex(1f)
                .width(80.dp)
        )
        Row(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .heightIn(min = 100.dp)
                .clip(shape)
                .background(type.containerColor)
                .padding(
                    start = 105.dp,
                    end = 4.dp,
                    top = 4.dp,
                    bottom = Theme.spacing.extraSmall
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = Theme.spacing.small)
            ) {
                Text(
                    text = stringResource(type.titleId),
                    style = Theme.typography.headline6,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = message,
                    style = Theme.typography.bodyMedium
                )
            }
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null
                )
            }
        }
    }
}

enum class FlashMessageType(
    @StringRes val titleId: Int,
    val containerColor: Color,
    @DrawableRes val iconId: Int,
    @DrawableRes val backgroundIconId: Int
) {
    SUCCESS(
        titleId = R.string.success_flash_message,
        containerColor = SuccessContainerColor,
        iconId = R.drawable.ic_success,
        backgroundIconId = R.drawable.ic_success_background
    ),
    ERROR(
        titleId = R.string.error_flash_message,
        containerColor = ErrorContainerColor,
        iconId = R.drawable.ic_error,
        backgroundIconId = R.drawable.ic_error_background
    ),
    WARNING(
        titleId = R.string.warning_flash_message,
        containerColor = WarningContainerColor,
        iconId = R.drawable.ic_warning,
        backgroundIconId = R.drawable.ic_warning_background
    ),
    HELP(
        titleId = R.string.help_flash_message,
        containerColor = HelpContainerColor,
        iconId = R.drawable.ic_help,
        backgroundIconId = R.drawable.ic_help_background
    )
}

@Preview
@Composable
private fun FlashMessageContainerPreview() {
    AppTheme {
        FlashMessageContainer(
            message = MessageText.Success(UiText.DynamicString("You successfully read this important message.")),
            onDismiss = {},
            content = { }
        )
    }
}

@Preview
@Composable
private fun SuccessFlashMessagePreview() {
    AppTheme {
        FlashMessage(
            message = "You successfully read this important message.",
            type = FlashMessageType.SUCCESS,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun ErrorFlashMessagePreview() {
    AppTheme {
        FlashMessage(
            message = "Change a few things up and try submitting again.",
            type = FlashMessageType.ERROR,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun WarningFlashMessagePreview() {
    AppTheme {
        FlashMessage(
            message = "Sorry! There was a problem with your request.",
            type = FlashMessageType.WARNING,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun HelpFlashMessagePreview() {
    AppTheme {
        FlashMessage(
            message = "Do you have a problem? Just use this contact form.",
            type = FlashMessageType.HELP,
            modifier = Modifier.fillMaxWidth()
        )
    }
}