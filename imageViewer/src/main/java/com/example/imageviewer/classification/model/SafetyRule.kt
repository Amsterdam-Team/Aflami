package com.example.imageviewer.classification.model

internal data class SafetyRule(
    val labelName: String,
    val labelIndex: Int,
    val threshold: Float
)