package com.jefisu.domain.util

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    data class StringResource(@StringRes val resId: Int, val args: Array<Any> = emptyArray()) :
        UiText

    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResource -> {
            val context = LocalContext.current
            val args = args.map { context.getStringFromArg(it) }.toTypedArray()
            stringResource(resId, *args)
        }
    }

    fun asString(context: Context): String = when (this) {
        is DynamicString -> value
        is StringResource -> {
            val args = args.map { context.getStringFromArg(it) }.toTypedArray()
            context.getString(resId, *args)
        }
    }

    private fun Context.getStringFromArg(arg: Any): Any {
        if (arg !is Int) return arg
        val result = runCatching { this.getString(arg) }
        return result.getOrNull() ?: "(Invalid arg resource: $arg)"
    }
}
