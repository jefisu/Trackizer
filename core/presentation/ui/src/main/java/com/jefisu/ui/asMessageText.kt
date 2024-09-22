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

    DataMessage.CATEGORY_NAME_CAN_T_BE_BLANK -> MessageText.Error(
        UiText.StringResource(R.string.category_can_t_be_blank),
    )

    DataMessage.CATEGORY_BUDGET_CAN_T_BE_ZERO -> MessageText.Error(
        UiText.StringResource(R.string.category_budget_can_t_be_zero),
    )

    DataMessage.CATEGORY_DELETED -> MessageText.Success(
        UiText.StringResource(R.string.category_deleted),
    )

    DataMessage.CATEGORY_NOT_DELETED -> MessageText.Error(
        UiText.StringResource(R.string.category_not_deleted),
    )

    DataMessage.SUBSCRIPTION_PRICE_CAN_T_BE_ZERO -> MessageText.Error(
        UiText.StringResource(R.string.subscription_price_can_t_be_zero),
    )

    DataMessage.FUNCTIONALITY_UNAVAILABLE -> MessageText.Warning(
        UiText.StringResource(R.string.functionality_unavailable),
    )
}
