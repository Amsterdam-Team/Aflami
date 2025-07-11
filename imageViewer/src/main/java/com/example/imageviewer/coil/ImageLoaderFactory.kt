package com.example.imageviewer.coil


import android.content.Context
import coil.ImageLoader
import com.example.imageviewer.classification.ImageClassifier
import com.example.imageviewer.classification.NSFWImageClassifier
import com.example.imageviewer.classification.policy.SafetyPolicy

internal object ImageLoaderFactory {

    fun create(context: Context, policy: SafetyPolicy): ImageLoader {
        val classifier: ImageClassifier = when (policy) {
            is SafetyPolicy.NudityPolicy -> NSFWImageClassifier(context)
        }

        return ImageLoader.Builder(context)
            .components {
                add(SafetyInterceptor(classifier))
            }
            .build()
    }
}