package com.example.designsystem.utils.modifierExtensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.LayoutDirection

fun Modifier.mirroredContent(layoutDirection: LayoutDirection): Modifier {
    val isRtl = layoutDirection == LayoutDirection.Rtl
    return this.scale(scaleX = if (isRtl) -1f else 1f, scaleY = 1f)
}
