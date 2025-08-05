package com.amsterdam.ui.utils

import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.imageviewer.classification.SafetyLevel

fun RestrictionLevel.toSafetyLevel(): SafetyLevel {
    return when(this){
        RestrictionLevel.STRICT -> SafetyLevel.STRICT
        RestrictionLevel.MODERATE -> SafetyLevel.MODERATE
        RestrictionLevel.OFF -> SafetyLevel.OFF
    }
}