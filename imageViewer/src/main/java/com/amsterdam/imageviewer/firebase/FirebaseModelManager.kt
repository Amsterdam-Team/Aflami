package com.amsterdam.imageviewer.firebase

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.IOException
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

internal object FirebaseModelManager {

    private const val MODEL_NAME = "NSFW"

    @Volatile
    var modelFile: File? = null
        private set

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val modelDownloadJob: Deferred<File?> by lazy {
        scope.async {
            downloadModel()
        }
    }
    suspend fun getModelFileInstance(): File? {
        return modelDownloadJob.await()
    }

    private suspend fun downloadModel(): File? {
        if (modelFile != null) return modelFile
        val conditions = CustomModelDownloadConditions.Builder().build()

        return run {
            FirebaseModelDownloader.getInstance()
                .getModel(MODEL_NAME, DownloadType.LOCAL_MODEL, conditions)
                .await()
        }.let {
            modelFile = it.file
            modelFile
        }
    }
}