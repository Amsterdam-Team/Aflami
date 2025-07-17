package com.example.designsystem.components.chip

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.designsystem.theme.AppTheme

object ChipDefaults {
    @Composable
    fun chipColors() =
        ChipColor(
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
    fun chipColors(
        iconSelectedColor: Color = Color.Unspecified,
        iconUnselectedColor: Color = Color.Unspecified,
        labelSelectedColor: Color = Color.Unspecified,
        labelUnselectedColor: Color = Color.Unspecified,
        borderSelectedColor: Color = Color.Unspecified,
        borderUnselectedColor: Color = Color.Unspecified,
        backgroundSelectedColor: Color = Color.Unspecified,
        backgroundUnselectedColor: Color = Color.Unspecified,
    ) = ChipColor(
        iconSelectedColor = iconSelectedColor,
        iconUnselectedColor = iconUnselectedColor,
        labelSelectedColor = labelSelectedColor,
        labelUnselectedColor = labelUnselectedColor,
        borderSelectedColor = borderSelectedColor,
        borderUnselectedColor = borderUnselectedColor,
        backgroundSelectedColor = backgroundSelectedColor,
        backgroundUnselectedColor = backgroundUnselectedColor,
    )
}

data class ChipColor(
    val iconSelectedColor: Color,
    val iconUnselectedColor: Color,
    val labelSelectedColor: Color,
    val labelUnselectedColor: Color,
    val borderSelectedColor: Color,
    val borderUnselectedColor: Color,
    val backgroundSelectedColor: Color,
    val backgroundUnselectedColor: Color,
)
