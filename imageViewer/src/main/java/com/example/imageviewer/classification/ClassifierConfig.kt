package com.example.imageviewer.classification

import com.example.imageviewer.classification.model.SafetyRule

internal object ClassifierConfig {
    const val NSFW_MODEL_PATH = "saved_model.tflite"
    val NSFW_SAFETY_RULES = listOf(
        SafetyRule(labelName = "Drawing", labelIndex = 0, threshold = 0.75f),
        SafetyRule(labelName = "Hentai", labelIndex = 1, threshold = 0.65f),
        SafetyRule(labelName = "Porn", labelIndex = 3, threshold = 0.50f),
        SafetyRule(labelName = "Sexy", labelIndex = 4, threshold = 0.50f)
    )

    const val LUMINANCE_THRESHOLD = 0.2f

    const val INPUT_IMAGE_WIDTH = 224
    const val INPUT_IMAGE_HEIGHT = 224
}