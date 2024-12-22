package com.jefisu.domain.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.jefisu.domain.util.Argument.Format

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    data class StringResource(@StringRes val resId: Int, val args: Array<Argument> = emptyArray()) :
        UiText

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> {
            val context = LocalContext.current
            val args = args.map { it.formatToDisplay(context) }.toTypedArray()
            stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> {
            val args = args.map { it.formatToDisplay(context) }.toTypedArray()
            context.getString(resId, *args)
        }
    }
}

data class Argument(val value: Any, val format: Format = Format.NORMAL) {

    fun formatToDisplay(context: Context): Any {
        val valueRes = context.getStringFromArg(value)
        if (valueRes !is String) return value
        return when (format) {
            Format.UPPERCASE -> valueRes.uppercase()
            Format.LOWERCASE -> valueRes.lowercase()
            Format.CAPITALIZE -> valueRes.replaceFirstChar { it.titlecase() }
            Format.NORMAL -> valueRes
        }
    }

    private fun Context.getStringFromArg(arg: Any): Any {
        if (arg !is Int) return arg
        val result = runCatching { this.getString(arg) }
        return result.getOrNull() ?: "(Invalid arg resource: $arg)"
    }

    enum class Format {
        NORMAL,
        UPPERCASE,
        LOWERCASE,
        CAPITALIZE,
    }
}