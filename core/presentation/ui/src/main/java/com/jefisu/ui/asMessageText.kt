package com.jefisu.ui

import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.UiText

fun DataMessage.asMessageText(): MessageText = when (this) {
    DataMessage.SUBSCRIPTION_DELETED -> MessageText.Success(
        UiText.StringResource(R.string.subscription_deleted),
    )

    DataMessage.SUBSCRIPTION_NOT_DELETED -> MessageText.Error(
        UiText.StringResource(R.string.subscription_not_deleted),
    )
}
