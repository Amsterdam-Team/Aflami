package com.example.designsystem.utils

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun ripple(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified,
): IndicationNodeFactory =
    androidx.compose.material3.ripple(
        bounded = bounded,
        radius = radius,
        color = color,
    )
