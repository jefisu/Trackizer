package com.jefisu.designsystem

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val interFontFamily = FontFamily(
    Font(R.font.inter, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_semibold, FontWeight.SemiBold),
    Font(R.font.inter_bold, FontWeight.Bold),
)

data class Typography(
    val display: TextStyle =
        TextStyle(
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 108.sp,
            fontFamily = interFontFamily,
        ),
    val headline8: TextStyle =
        TextStyle(
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 56.sp,
            fontFamily = interFontFamily,
        ),
    val headline7: TextStyle =
        TextStyle(
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp,
            fontFamily = interFontFamily,
        ),
    val headline6: TextStyle =
        TextStyle(
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 48.sp,
            fontFamily = interFontFamily,
        ),
    val headline5: TextStyle =
        TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 36.sp,
            fontFamily = interFontFamily,
        ),
    val headline4: TextStyle =
        TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 32.sp,
            fontFamily = interFontFamily,
        ),
    val headline3: TextStyle =
        TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 24.sp,
            fontFamily = interFontFamily,
        ),
    val headline2: TextStyle =
        TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.sp,
            fontFamily = interFontFamily,
        ),
    val headline1: TextStyle =
        TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp,
            fontFamily = interFontFamily,
        ),
    val subtitle: TextStyle =
        TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 32.sp,
            fontFamily = interFontFamily,
        ),
    val bodyLarge: TextStyle =
        TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 24.sp,
            fontFamily = interFontFamily,
        ),
    val bodyMedium: TextStyle =
        TextStyle(
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            lineHeight = 20.sp,
            fontFamily = interFontFamily,
        ),
    val bodySmall: TextStyle =
        TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp,
            fontFamily = interFontFamily,
        ),
)

val TrackizerTheme.typography: Typography
    get() = Typography()