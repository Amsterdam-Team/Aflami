package com.example.designsystem.utils.modifierExtensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

fun Modifier.autoMirroredContent(): Modifier =
    composed {
        val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
        this.scale(scaleX = if (isRtl) -1f else 1f, scaleY = 1f)
    }
