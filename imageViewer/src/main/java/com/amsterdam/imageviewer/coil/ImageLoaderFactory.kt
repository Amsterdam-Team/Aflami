package com.amsterdam.imageviewer.coil


import android.content.Context
import coil.ImageLoader
import com.amsterdam.imageviewer.classification.ImageClassifier
import com.amsterdam.imageviewer.classification.SFWImageClassifier
import com.amsterdam.imageviewer.classification.policy.SafetyPolicy

internal object ImageLoaderFactory {

        fun create(context: Context, policy: SafetyPolicy): ImageLoader {
            val classifier: ImageClassifier = when (policy) {
                is SafetyPolicy.SFWPolicy -> SFWImageClassifier(context)
            }

            return ImageLoader.Builder(context)
                .components {
                    add(SafetyInterceptor(classifier))
                }
                .build()
        }
    }