package com.amsterdam.imageviewer.coil

import android.content.Context
import coil.ImageLoader
import com.amsterdam.imageviewer.classification.NsfwDetectorClassifier

internal fun createImageLoader(
    context: Context,
    classifier: NsfwDetectorClassifier?
): ImageLoader? {
    if (classifier == null) return null

    return ImageLoader.Builder(context)
        .components { add(SafetyInterceptor(classifier)) }
        .build()
}