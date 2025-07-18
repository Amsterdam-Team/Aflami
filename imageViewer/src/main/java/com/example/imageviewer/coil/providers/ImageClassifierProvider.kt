package com.example.imageviewer.coil.providers

import com.example.imageviewer.classification.SFWImageClassifier
import com.example.imageviewer.firebase.FirebaseModelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tensorflow.lite.Interpreter

object ImageClassifierProvider {

    @Volatile
    private var instance: SFWImageClassifier? = null
    private val mutex = Mutex()

    suspend fun getInstance(): SFWImageClassifier {
        return instance ?: mutex.withLock {
            instance ?: createInstance().also {
                instance = it
            }
        }
    }

    private suspend fun createInstance(): SFWImageClassifier {
        val modelFile = FirebaseModelManager.modelFile
        val options = Interpreter.Options().apply {
            setUseNNAPI(true)
        }

        val interpreter = if (modelFile != null) {
            Interpreter(modelFile, options)
        } else {
            val attempt = CoroutineScope(Dispatchers.IO).async {
                FirebaseModelManager.getModelFileInstance()
            }
            Interpreter(attempt.await(), options)
        }

        return SFWImageClassifier(interpreter)
    }
}