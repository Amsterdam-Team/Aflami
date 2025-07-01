package com.amsterdam.aflami.theme.textStyle

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.amsterdam.aflami.R

private val poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
)
private val nicomoji = FontFamily(
    Font(R.font.nicomoji_regular, FontWeight.Normal),
)
internal val defaultTextStyle = AflamiTextStyle(
    headline = SizedTextStyle(
        large = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 42.sp,

            ),
        medium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 36.sp,
        ),
        small = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 30.sp,
        )
    ),
    title = SizedTextStyle(
        large = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 30.sp,
        ),
        medium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            lineHeight = 28.sp,
        ),
        small = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        )

    ),
    body = SizedTextStyle(
        large = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 28.sp,
        ),
        medium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 26.sp,
        ),
        small = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
        )
    ),
    label = SizedTextStyle(
        large = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        medium = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        small = TextStyle(
            fontFamily = poppins,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            lineHeight = 16.sp,
        )
    ),
    appName = SizedTextStyle(
        large = TextStyle(
            fontFamily = nicomoji,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        medium = TextStyle(
            fontFamily = nicomoji,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
        small = TextStyle(
            fontFamily = nicomoji,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 30.sp,
        )
    ),
    appLogo = SizedTextStyle(
        large = TextStyle(
            fontFamily = nicomoji,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 20.sp,
        ),
        medium = TextStyle(
            fontFamily = nicomoji,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 20.sp,
        ),
        small = TextStyle(
            fontFamily = nicomoji,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 20.sp,
        )
    ),
)