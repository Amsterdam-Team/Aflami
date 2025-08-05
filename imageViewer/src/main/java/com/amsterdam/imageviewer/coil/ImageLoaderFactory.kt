package com.amsterdam.imageviewer.coil

import android.content.Context
import coil.ImageLoader
import com.amsterdam.imageviewer.classification.NsfwDetectorClassifier
import com.amsterdam.imageviewer.classification.NsfwDetectorConfig
import com.amsterdam.imageviewer.classification.SafetyLevel
import com.amsterdam.imageviewer.classification.model.NsfwDetectorRule

internal fun createImageLoader(
    context: Context,
    classifier: NsfwDetectorClassifier?,
    safetyLevel: SafetyLevel
): ImageLoader? {
    if (classifier == null) return null
    val nsfwDetectorRule = if (safetyLevel == SafetyLevel.STRICT) NsfwDetectorConfig.NSFW_DETECTION_RULE_STRICT else NsfwDetectorConfig.NSFW_DETECTION_RULE_MODERATE
    return ImageLoader.Builder(context)
        .components { add(SafetyInterceptor(classifier, nsfwDetectorRule)) }
        .build()
}