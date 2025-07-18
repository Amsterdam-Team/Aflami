package com.example.designsystem.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.designsystem.theme.AppTheme

object ButtonDefaults {
    @Composable
    fun buttonColors() =
        ButtonColors(
            containerColor = AppTheme.color.onPrimary,
            secondaryContainerColor = AppTheme.color.primary,
            negativeContainerColor = AppTheme.color.redAccent,
            disableContainerColor = AppTheme.color.stroke,
        )

    @Composable
    fun buttonColors(
        containerColor: Color = Color.Unspecified,
        secondaryContainerColor: Color = Color.Unspecified,
        negativeContainerColor: Color = Color.Unspecified,
        disableContainerColor: Color = Color.Unspecified,
    ) = ButtonColors(
        containerColor = containerColor,
        secondaryContainerColor = secondaryContainerColor,
        negativeContainerColor = negativeContainerColor,
        disableContainerColor = disableContainerColor,
    )

    @Composable
    fun brushColors() =
        ButtonBrushColor(
            startColor = AppTheme.color.primary,
            endColor = AppTheme.color.primaryEnd,
            secondaryColor = AppTheme.color.primaryVariant,
            negativeColor = AppTheme.color.redVariant,
            disableColor = AppTheme.color.disable,
        )

    @Composable
    fun brushColors(
        startColor: Color = Color.Unspecified,
        endColor: Color = Color.Unspecified,
        secondaryColor: Color = Color.Unspecified,
        negativeColor: Color = Color.Unspecified,
        disableColor: Color = Color.Unspecified,
    ) = ButtonBrushColor(
        startColor = startColor,
        endColor = endColor,
        secondaryColor = secondaryColor,
        negativeColor = negativeColor,
        disableColor = disableColor,
    )

    @Composable
    fun textButtonColors() =
        ButtonColors(
            containerColor = AppTheme.color.primary,
            secondaryContainerColor = Color.Unspecified,
            negativeContainerColor = AppTheme.color.redAccent,
            disableContainerColor = AppTheme.color.disable,
        )
}

data class ButtonColors(
    val containerColor: Color,
    val secondaryContainerColor: Color,
    val negativeContainerColor: Color,
    val disableContainerColor: Color,
)

data class ButtonBrushColor(
    val startColor: Color,
    val endColor: Color,
    val secondaryColor: Color,
    val negativeColor: Color,
    val disableColor: Color,
)
