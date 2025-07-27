package com.amsterdam.imageviewer.coil


import android.content.Context
import coil.ImageLoader
import com.amsterdam.imageviewer.classification.CustomImageClassifier
import com.amsterdam.imageviewer.classification.NsfwDetectorClassifier
import com.amsterdam.imageviewer.classification.policy.SafetyPolicy

internal object ImageLoaderFactory {

    fun build(
        context: Context,
        classifier: NsfwDetectorClassifier?,
        policy: SafetyPolicy
    ): ImageLoader? {
        if (classifier == null) return null
        val classifier: CustomImageClassifier = when (policy) {
            is SafetyPolicy.SFWPolicy -> classifier
        }

        return ImageLoader.Builder(context).components {
            add(SafetyInterceptor(classifier))
        }.build()
    }
}