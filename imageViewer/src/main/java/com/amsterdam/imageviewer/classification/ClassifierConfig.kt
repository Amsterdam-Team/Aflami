package com.amsterdam.imageviewer.classification

import com.amsterdam.imageviewer.classification.model.NsfwDetectorRule

internal object NsfwDetectorConfig {
    val NSFW_DETECTION_RULE = NsfwDetectorRule(
        nudeIndex = 1,
        nonNudeIndex = 0,
        nsfwThreshold = 0.2f,
        sfwThreshold = 0.4f
    )
}