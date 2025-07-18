package com.example.imageviewer.classification

import com.example.imageviewer.classification.model.SafetyRule

internal object SFWClassifierConfig {
    val NSFW_SAFETY_RULES = listOf(
        SafetyRule(labelName = "porn", labelIndex = 2, threshold = 0.01f),
        SafetyRule(labelName = "sexy", labelIndex = 3, threshold = 0.01f)
    )
    const val INPUT_IMAGE_WIDTH = 224
    const val INPUT_IMAGE_HEIGHT = 224
}