package com.example.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.designsystem.theme.colors.AflamiColorScheme
import com.example.designsystem.theme.colors.LocalAflamiAppColors
import com.example.designsystem.theme.textStyle.AflamiTextStyle
import com.example.designsystem.theme.textStyle.LocalAflamiTextStyle

typealias GradientType = @Composable () -> List<Color>

typealias ColorType = @Composable () -> Color

object AppTheme {
    val color: AflamiColorScheme
        @Composable @ReadOnlyComposable
        get() = LocalAflamiAppColors.current

    val textStyle: AflamiTextStyle
        @Composable @ReadOnlyComposable
        get() = LocalAflamiTextStyle.current
}
