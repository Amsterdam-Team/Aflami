package com.example.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.designsystem.theme.colors.AflamiColorScheme
import com.example.designsystem.theme.colors.localAflamiAppColors
import com.example.designsystem.theme.textStyle.AflamiTextStyle
import com.example.designsystem.theme.textStyle.LocalAflamiTextStyle

typealias ColorType = @Composable () -> Color

object AppTheme{
    val color: AflamiColorScheme
    @Composable @ReadOnlyComposable get() = localAflamiAppColors.current

    val textStyle: AflamiTextStyle
    @Composable @ReadOnlyComposable get() = LocalAflamiTextStyle.current
//  val images: AflamiAppImages
//      @Composable @ReadOnlyComposable get() = LocalAflamiLocalImages.current
}