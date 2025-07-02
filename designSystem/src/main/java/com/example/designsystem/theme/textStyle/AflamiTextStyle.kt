package com.example.designsystem.theme.textStyle

import androidx.compose.ui.text.TextStyle

data class AflamiTextStyle(
    val headline: SizedTextStyle,
    val title: SizedTextStyle,
    val body: SizedTextStyle,
    val label: SizedTextStyle,
    val appName: SizedTextStyle,
    val appLogo: SizedTextStyle,
)


data class SizedTextStyle(
    val large: TextStyle,
    val medium: TextStyle,
    val small: TextStyle,
)

