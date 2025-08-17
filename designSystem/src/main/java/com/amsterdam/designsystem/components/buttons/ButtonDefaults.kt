package com.amsterdam.designsystem.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.amsterdam.designsystem.theme.AppTheme

object ButtonDefaults {
    @Composable
    fun buttonColors(
        containerColor: Color = AppTheme.color.onPrimary,
        secondaryContainerColor: Color = AppTheme.color.primary,
        negativeContainerColor: Color = AppTheme.color.redAccent,
        disableContainerColor: Color = AppTheme.color.stroke,
    ) =
        ButtonColors(
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
