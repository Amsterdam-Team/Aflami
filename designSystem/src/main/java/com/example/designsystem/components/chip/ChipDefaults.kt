package com.example.designsystem.components.chip

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.designsystem.theme.AppTheme

object ChipDefaults {
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
    fun chipColors(
        iconSelectedColor: Color = Color.Unspecified,
        iconUnselectedColor: Color = Color.Unspecified,
        labelSelectedColor: Color = Color.Unspecified,
        labelUnselectedColor: Color = Color.Unspecified,
        borderSelectedColor: Color = Color.Unspecified,
        borderUnselectedColor: Color = Color.Unspecified,
        backgroundSelectedColor: Color = Color.Unspecified,
        backgroundUnselectedColor: Color = Color.Unspecified,
    ) = ChipColors(
        iconSelectedColor = iconSelectedColor,
        iconUnselectedColor = iconUnselectedColor,
        labelSelectedColor = labelSelectedColor,
        labelUnselectedColor = labelUnselectedColor,
        borderSelectedColor = borderSelectedColor,
        borderUnselectedColor = borderUnselectedColor,
        backgroundSelectedColor = backgroundSelectedColor,
        backgroundUnselectedColor = backgroundUnselectedColor,
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

    @Composable
    fun genreChipColors(
        textSelectedColor: Color = Color.Unspecified,
        textUnselectedColor: Color = Color.Unspecified,
        boxSelectedColor: Color = Color.Unspecified,
        boxUnselectedColor: Color = Color.Unspecified,
    ) = ChipColors(
        iconSelectedColor = Color.Unspecified,
        iconUnselectedColor = Color.Unspecified,
        labelSelectedColor = textSelectedColor,
        labelUnselectedColor = textUnselectedColor,
        borderSelectedColor = Color.Unspecified,
        borderUnselectedColor = Color.Unspecified,
        backgroundSelectedColor = boxSelectedColor,
        backgroundUnselectedColor = boxUnselectedColor,
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
