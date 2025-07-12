package com.example.imageviewer.classification

import com.example.imageviewer.classification.model.SafetyRule

internal object SFWClassifierConfig {
    const val NSFW_MODEL_PATH = "sfw.tflite"
    val NSFW_SAFETY_RULES = listOf(
        SafetyRule(labelName = "drawing", labelIndex = 0, threshold = 0.75f),
        SafetyRule(labelName = "porn", labelIndex = 3, threshold = 0.30f),
        SafetyRule(labelName = "sexy", labelIndex = 4, threshold = 0.30f)
    )

    const val LUMINANCE_THRESHOLD = 0.2f

    const val INPUT_IMAGE_WIDTH = 224
    const val INPUT_IMAGE_HEIGHT = 224
}