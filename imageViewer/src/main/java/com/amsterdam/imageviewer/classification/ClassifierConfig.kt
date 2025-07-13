package com.amsterdam.imageviewer.classification

import com.amsterdam.imageviewer.classification.model.SafetyRule

internal object SFWClassifierConfig {
    const val NSFW_MODEL_PATH = "sfw.tflite"
    val NSFW_SAFETY_RULES = listOf(
        SafetyRule(labelName = "porn", labelIndex = 2, threshold = 0.01f),
        SafetyRule(labelName = "sexy", labelIndex = 3, threshold = 0.01f)
    )
    const val INPUT_IMAGE_WIDTH = 224
    const val INPUT_IMAGE_HEIGHT = 224
}