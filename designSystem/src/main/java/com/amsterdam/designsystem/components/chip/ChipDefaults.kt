package com.amsterdam.designsystem.components.chip

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amsterdam.designsystem.theme.AppTheme

internal object ChipDefaults {
    @Composable
    fun chipColors() =
        ChipColors(
            iconSelectedColor = AppTheme.color.onPrimary,
            iconUnselectedColor = AppTheme.color.hint,
            labelSelectedColor = AppTheme.color.body,
            labelUnselectedColor = AppTheme.color.hint,
            borderSelectedColor = AppTheme.color.stroke,
            borderUnselectedColor = AppTheme.color.surfaceHigh,
            backgroundSelectedColor = AppTheme.color.secondary,
            backgroundUnselectedColor = AppTheme.color.surfaceHigh,
        )

    @Composable
    fun genreChipColors() =
        ChipColors(
            iconSelectedColor = Color.Unspecified,
            iconUnselectedColor = Color.Unspecified,
            labelSelectedColor = AppTheme.color.onPrimary,
            labelUnselectedColor = AppTheme.color.primary,
            borderSelectedColor = Color.Unspecified,
            borderUnselectedColor = Color.Unspecified,
            backgroundSelectedColor = AppTheme.color.primary,
            backgroundUnselectedColor = AppTheme.color.surfaceHigh,
        )
}

data class ChipColors(
    val iconSelectedColor: Color,
    val iconUnselectedColor: Color,
    val labelSelectedColor: Color,
    val labelUnselectedColor: Color,
    val borderSelectedColor: Color,
    val borderUnselectedColor: Color,
    val backgroundSelectedColor: Color,
    val backgroundUnselectedColor: Color,
)
