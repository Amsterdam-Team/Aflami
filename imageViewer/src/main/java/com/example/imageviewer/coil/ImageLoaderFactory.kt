package com.example.imageviewer.coil


import android.content.Context
import coil.ImageLoader
import com.example.imageviewer.classification.ImageClassifier
import com.example.imageviewer.classification.SFWImageClassifier
import com.example.imageviewer.classification.policy.SafetyPolicy

internal object ImageLoaderFactory {

    fun build(
        context: Context,
        classifier: SFWImageClassifier,
        policy: SafetyPolicy
    ): ImageLoader {
        val classifier: ImageClassifier = when (policy) {
            is SafetyPolicy.SFWPolicy -> classifier
        }

        return ImageLoader.Builder(context).components {
                add(SafetyInterceptor(classifier))
            }.build()
    }
}