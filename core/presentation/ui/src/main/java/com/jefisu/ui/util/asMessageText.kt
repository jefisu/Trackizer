package com.jefisu.ui.util

import com.jefisu.domain.util.Argument
import com.jefisu.domain.util.DataMessage
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.MessageText.Error
import com.jefisu.domain.util.MessageText.Success
import com.jefisu.domain.util.MessageText.Warning
import com.jefisu.domain.util.UiText.StringResource
import com.jefisu.ui.R

fun DataMessage.asMessageText(vararg args: Argument = emptyArray()): MessageText = when (this) {
    DataMessage.SUBSCRIPTION_REMOVED -> Success(
        StringResource(
            R.string.item_deleted,
            arrayOf(Argument(R.string.subscription, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.SUBSCRIPTION_REMOVAL_FAILED -> Error(
        StringResource(
            R.string.item_not_deleted,
            arrayOf(Argument(R.string.subscription, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.CATEGORY_NAME_REQUIRED -> Error(
        StringResource(
            R.string.item_can_t_be_blank,
            arrayOf(Argument(R.string.category_name, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.CATEGORY_BUDGET_REQUIRED -> Error(
        StringResource(
            R.string.data_can_t_be_zero,
            arrayOf(Argument(R.string.category_budget, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.CATEGORY_REMOVED -> Success(
        StringResource(
            R.string.item_deleted,
            arrayOf(Argument(R.string.category, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.CATEGORY_REMOVAL_FAILED -> Error(
        StringResource(
            R.string.item_not_deleted,
            arrayOf(Argument(R.string.category, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.SUBSCRIPTION_PRICE_REQUIRED -> Error(
        StringResource(
            R.string.data_can_t_be_zero,
            arrayOf(Argument(R.string.subscription_price, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.FUNCTIONALITY_UNAVAILABLE -> Warning(
        StringResource(R.string.functionality_unavailable),
    )

    DataMessage.CARD_ADDITION_FAILED -> Error(
        StringResource(
            R.string.item_not_added,
            arrayOf(Argument(R.string.card, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.CARD_REMOVAL_FAILED -> Error(
        StringResource(
            R.string.item_not_deleted,
            arrayOf(Argument(R.string.card, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.CATEGORY_ADDITION_FAILED -> Error(
        StringResource(
            R.string.item_not_added,
            arrayOf(Argument(R.string.category, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.SUBSCRIPTION_ADDITION_FAILED -> Error(
        StringResource(
            R.string.item_not_added,
            arrayOf(Argument(R.string.subscription, Argument.Format.LOWERCASE)),
        ),
    )

    DataMessage.UNKNOWN_ERROR -> Error(
        StringResource(R.string.unknown_error),
    )

    DataMessage.NO_DATA_AVAILABLE -> Warning(
        StringResource(
            R.string.data_not_available,
            args.toList().toTypedArray(),
        ),
    )

    DataMessage.INCORRECT_PASSWORD -> Error(
        StringResource(R.string.incorrect_password_error),
    )

    DataMessage.DELETE_ACCOUNT_FAILED -> Error(
        StringResource(R.string.delete_account_failed_error),
    )
}
