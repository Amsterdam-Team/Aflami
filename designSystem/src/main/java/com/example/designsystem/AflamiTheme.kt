package com.example.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.amsterdam.aflami.theme.textStyle.AflamiTextStyle
import com.amsterdam.aflami.theme.textStyle.LocalAflamiTextStyle

typealias ColorType = @Composable () -> Color

object AflamiTheme{
// val color: AflamiAppColor
//     @Composable @ReadOnlyComposable get() = LocalAflamiLocalColors.current

val textStyle: AflamiTextStyle
    @Composable @ReadOnlyComposable get() = LocalAflamiTextStyle.current
//  val images: AflamiAppImages
//      @Composable @ReadOnlyComposable get() = LocalAflamiLocalImages.current
}