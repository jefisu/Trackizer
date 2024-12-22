package com.jefisu.designsystem.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormatSymbols
import java.util.Locale

internal class CurrencyVisualTransformation(private val locale: Locale = Locale.getDefault()) :
    VisualTransformation {

    private val symbols = DecimalFormatSymbols(locale).apply {
        currencySymbol = currencySymbol.removePrefix(locale.country)
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val numberOfDecimals = 2
        val currencySymbol = symbols.currencySymbol
        val thousandsSeparator = symbols.groupingSeparator
        val decimalSeparator = symbols.decimalSeparator
        val zero = symbols.zeroDigit

        val originalText = text.text

        val intPart = originalText
            .dropLast(numberOfDecimals)
            .reversed()
            .chunked(3)
            .joinToString(thousandsSeparator.toString())
            .reversed()
            .ifEmpty(zero::toString)

        val fractionPart = originalText.takeLast(numberOfDecimals).let {
            if (it.length != numberOfDecimals) {
                return@let List(numberOfDecimals - it.length) {
                    zero
                }.joinToString("") + it
            }
            it
        }

        val formattedNumber = intPart + decimalSeparator + fractionPart

        val newText = AnnotatedString(
            text = "$currencySymbol $formattedNumber",
            spanStyles = text.spanStyles,
            paragraphStyles = text.paragraphStyles,
        )

        return TransformedText(
            newText,
            FixedCursorOffsetMapping(
                contentLength = originalText.length,
                formattedContentLength = newText.length,
            ),
        )
    }
}

private class FixedCursorOffsetMapping(
    private val contentLength: Int,
    private val formattedContentLength: Int,
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = formattedContentLength
    override fun transformedToOriginal(offset: Int): Int = contentLength
}