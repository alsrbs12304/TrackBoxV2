package com.mgpark.trackbox.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val AppFontFamily: FontFamily = FontFamily.Default

private val Display = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.SemiBold,
)

private val Headline = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.SemiBold,
)

private val Title = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Medium,
)

private val Body = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Normal,
)

private val Label = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = FontWeight.Medium,
)

internal val AppTypography = Typography(
    displayLarge   = Display.copy(fontSize = 57.sp, lineHeight = 64.sp, letterSpacing = (-0.25).sp),
    displayMedium  = Display.copy(fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall   = Display.copy(fontSize = 36.sp, lineHeight = 44.sp),

    headlineLarge  = Headline.copy(fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = Headline.copy(fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall  = Headline.copy(fontSize = 24.sp, lineHeight = 32.sp),

    titleLarge     = Title.copy(fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium    = Title.copy(fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.15.sp),
    titleSmall     = Title.copy(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),

    bodyLarge      = Body.copy(fontSize = 16.sp, lineHeight = 24.sp, letterSpacing = 0.5.sp),
    bodyMedium     = Body.copy(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.25.sp),
    bodySmall      = Body.copy(fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.4.sp),

    labelLarge     = Label.copy(fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.1.sp),
    labelMedium    = Label.copy(fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
    labelSmall     = Label.copy(fontSize = 11.sp, lineHeight = 16.sp, letterSpacing = 0.5.sp),
)
