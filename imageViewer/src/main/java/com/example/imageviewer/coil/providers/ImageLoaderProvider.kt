package com.example.imageviewer.coil.providers

import android.content.Context
import coil.ImageLoader
import com.example.imageviewer.classification.policy.SafetyPolicy
import com.example.imageviewer.coil.ImageLoaderFactory
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object ImageLoaderProvider {

    @Volatile
    private var instance: ImageLoader? = null
    private val mutex = Mutex()

    suspend fun getInstance(context: Context): ImageLoader {
        return instance ?: mutex.withLock {
            instance ?: createInstance(context.applicationContext)
        }
    }

    private suspend fun createInstance(context: Context): ImageLoader {
        val classifier = ImageClassifierProvider.getInstance()
        return ImageLoaderFactory.build(context, classifier, SafetyPolicy.SFWPolicy).also {
            instance = it
        }
    }
}