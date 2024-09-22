package com.jefisu.designsystem.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.jefisu.designsystem.ErrorContainerColor
import com.jefisu.designsystem.HelpContainerColor
import com.jefisu.designsystem.R
import com.jefisu.designsystem.SuccessContainerColor
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.WarningContainerColor
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.UiText

@Composable
fun FlashMessageDialog(
    message: MessageText?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    message?.let { msg ->
        val flashMessageType = when (message) {
            is MessageText.Error -> FlashMessageType.ERROR
            is MessageText.Warning -> FlashMessageType.WARNING
            is MessageText.Help -> FlashMessageType.HELP
            is MessageText.Success -> FlashMessageType.SUCCESS
        }

        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
            ),
        ) {
            Box(modifier = modifier.fillMaxSize()) {
                FlashMessage(
                    message = msg.text.asString(),
                    type = flashMessageType,
                    onCloseClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = TrackizerTheme.spacing.extraMedium)
                        .padding(bottom = TrackizerTheme.spacing.medium),
                )
            }
        }
    }
}

@Composable
internal fun FlashMessage(
    modifier: Modifier = Modifier,
    message: String,
    type: FlashMessageType,
    onCloseClick: () -> Unit = {},
) {
    val shape = RoundedCornerShape(32.dp)

    Box(
        modifier = modifier.clip(shape),
    ) {
        Image(
            painter = painterResource(id = type.iconId),
            contentDescription = null,
            modifier = Modifier
                .zIndex(1f)
                .padding(start = 20.dp)
                .size(58.dp),
        )
        Image(
            painter = painterResource(id = type.backgroundIconId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .zIndex(1f)
                .width(80.dp),
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
                    bottom = TrackizerTheme.spacing.extraSmall,
                ),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = TrackizerTheme.spacing.small),
            ) {
                Text(
                    text = stringResource(type.titleId),
                    style = TrackizerTheme.typography.headline6,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = message,
                    style = TrackizerTheme.typography.bodyMedium,
                )
            }
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                )
            }
        }
    }
}

internal enum class FlashMessageType(
    @StringRes val titleId: Int,
    val containerColor: Color,
    @DrawableRes val iconId: Int,
    @DrawableRes val backgroundIconId: Int,
) {
    SUCCESS(
        titleId = R.string.success_flash_message,
        containerColor = SuccessContainerColor,
        iconId = R.drawable.ic_success,
        backgroundIconId = R.drawable.ic_success_background,
    ),
    ERROR(
        titleId = R.string.error_flash_message,
        containerColor = ErrorContainerColor,
        iconId = R.drawable.ic_error,
        backgroundIconId = R.drawable.ic_error_background,
    ),
    WARNING(
        titleId = R.string.warning_flash_message,
        containerColor = WarningContainerColor,
        iconId = R.drawable.ic_warning,
        backgroundIconId = R.drawable.ic_warning_background,
    ),
    HELP(
        titleId = R.string.help_flash_message,
        containerColor = HelpContainerColor,
        iconId = R.drawable.ic_help,
        backgroundIconId = R.drawable.ic_help_background,
    ),
}

private val messages = listOf(
    "You successfully read this important message." to FlashMessageType.SUCCESS,
    "Change a few things up and try submitting again." to FlashMessageType.ERROR,
    "Sorry! There was a problem with your request." to FlashMessageType.WARNING,
    "Do you have a problem? Just use this contact form." to FlashMessageType.HELP,
)

@Preview
@Composable
private fun FlashMessageDialogPreview() {
    TrackizerTheme {
        FlashMessageDialog(
            message = MessageText.Success(UiText.DynamicString(messages.first().first)),
            onDismiss = {},
        )
    }
}

private class FlashMessagePreviewParameter :
    PreviewParameterProvider<Pair<String, FlashMessageType>> {

    override val values: Sequence<Pair<String, FlashMessageType>>
        get() = messages.asSequence()
}

@Preview
@Composable
private fun FlashMessagePreview(
    @PreviewParameter(FlashMessagePreviewParameter::class) message: Pair<String, FlashMessageType>,
) {
    TrackizerTheme {
        FlashMessage(
            message = message.first,
            type = message.second,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
