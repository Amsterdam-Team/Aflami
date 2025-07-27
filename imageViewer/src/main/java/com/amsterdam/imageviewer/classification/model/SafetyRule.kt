package com.amsterdam.imageviewer.classification.model


internal data class NsfwDetectorRule(
    val nudeIndex: Int,
    val nonNudeIndex: Int,
    val nsfwThreshold: Float,
    val sfwThreshold: Float
)
