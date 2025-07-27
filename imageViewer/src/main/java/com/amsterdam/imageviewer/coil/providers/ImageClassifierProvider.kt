package com.amsterdam.imageviewer.coil.providers

import android.content.Context
import com.amsterdam.imageviewer.classification.NsfwDetectorClassifier
import com.amsterdam.imageviewer.firebase.FirebaseModelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

internal object ImageClassifierProvider {

    @Volatile
    private var instance: NsfwDetectorClassifier? = null
    private val mutex = Mutex()

    suspend fun getInstance(context: Context): NsfwDetectorClassifier? {
        return instance ?: mutex.withLock {
            instance ?: createInstance(context.applicationContext).also {
                instance = it
            }
        }
    }

    private suspend fun createInstance(context: Context): NsfwDetectorClassifier? {
       // val modelFile = FirebaseModelManager.modelFile
        val attempt = CoroutineScope(Dispatchers.IO).async {
            FirebaseModelManager.getModelFileInstance()
        }
        //val modelFile = FirebaseModelManager.getModelFileInstance()
        val modelFile = attempt.await()
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(2)
            .build()

        val imageClassifier = ImageClassifier.createFromFileAndOptions(
            modelFile,
            options
        )

        return NsfwDetectorClassifier(imageClassifier)
    }
}