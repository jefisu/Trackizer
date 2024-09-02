package com.jefisu.auth.presentation.util

import com.jefisu.auth.R
import com.jefisu.auth.domain.AuthMessage
import com.jefisu.auth.domain.validation.EmailValidateError
import com.jefisu.auth.domain.validation.PassswordValidateError
import com.jefisu.domain.util.MessageText
import com.jefisu.domain.util.UiText

fun AuthMessage.asMessageText(): MessageText = when (this) {
    is AuthMessage.Error -> MessageText.Error(
        when (this) {
            AuthMessage.Error.INVALID_EMAIL_OR_PASSWORD -> UiText.StringResource(
                R.string.invalid_email_or_password_error,
            )

            AuthMessage.Error.USER_ALREADY_EXISTS -> UiText.StringResource(
                R.string.user_already_exists_error,
            )

            AuthMessage.Error.GOOGLE_FAILED_TO_LOGIN -> UiText.StringResource(
                R.string.failed_to_login_with_account_error,
                arrayOf("Google"),
            )

            AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN -> UiText.StringResource(
                R.string.failed_to_login_with_account_error,
                arrayOf("Facebook"),
            )

            AuthMessage.Error.SERVER_ERROR -> UiText.StringResource(R.string.server_error)
            AuthMessage.Error.USER_NOT_FOUND -> UiText.StringResource(
                R.string.user_not_found_error,
            )

            AuthMessage.Error.INTERNET_UNAVAILABLE -> UiText.StringResource(
                R.string.internet_unavailable_error,
            )
        },
    )

    is AuthMessage.Success -> MessageText.Success(
        when (this) {
            AuthMessage.Success.SENT_PASSWORD_RESET_EMAIL -> UiText.StringResource(
                R.string.sent_password_reset_email,
            )
        },
    )
}

fun EmailValidateError.asMessageText(): MessageText = MessageText.Error(
    when (this) {
        EmailValidateError.CAN_T_BE_BLANK -> UiText.StringResource(
            R.string.email_cant_be_blank_error,
        )

        EmailValidateError.INVALID_FORMAT -> UiText.StringResource(
            R.string.invalid_email_format_error,
        )
    },
)

fun PassswordValidateError.asMessageText(): MessageText = MessageText.Error(
    UiText.StringResource(R.string.password_error),
)
