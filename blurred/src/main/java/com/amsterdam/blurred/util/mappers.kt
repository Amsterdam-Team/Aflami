package com.amsterdam.blurred.util

import androidx.compose.ui.draw.BlurredEdgeTreatment
import com.amsterdam.blurred.blurProcessor.BlurEdgeTreatment

internal fun BlurEdgeTreatment.toBlurredEdgeTreatment(): BlurredEdgeTreatment =
    if (this == BlurEdgeTreatment.RECTANGLE) BlurredEdgeTreatment.Rectangle else BlurredEdgeTreatment.Unbounded