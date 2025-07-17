package com.example.imageviewer.coil


import android.content.Context
import coil.ImageLoader
import com.example.imageviewer.classification.ImageClassifier
import com.example.imageviewer.classification.SFWImageClassifier
import com.example.imageviewer.classification.policy.SafetyPolicy
import com.example.imageviewer.firebase.FirebaseModelManager

internal object ImageLoaderFactory {

        fun create(context: Context, policy: SafetyPolicy): ImageLoader {
            val classifier: ImageClassifier = when (policy) {
                is SafetyPolicy.SFWPolicy -> SFWImageClassifier(context,modelRepository = FirebaseModelManager)
            }

            return ImageLoader.Builder(context)
                .components {
                    add(SafetyInterceptor(classifier))
                }
                .build()
        }
    }