package com.amsterdam.imageviewer.classification

import com.amsterdam.imageviewer.classification.model.NsfwDetectorRule

internal object NsfwDetectorConfig {
    val NSFW_DETECTION_RULE_STRICT = NsfwDetectorRule(
        nudeIndex = 1,
        nonNudeIndex = 0,
        nsfwThreshold = 0.2f,
        sfwThreshold = 0.4f
    )

    val NSFW_DETECTION_RULE_MODERATE = NsfwDetectorRule(
        nudeIndex = 1,
        nonNudeIndex = 0,
        nsfwThreshold = 0.5f,
        sfwThreshold = 0.7f
    )
}