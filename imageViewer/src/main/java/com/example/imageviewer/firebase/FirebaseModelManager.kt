package com.example.imageviewer.firebase

import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.IOException

object FirebaseModelManager {

    private const val MODEL_NAME = "NSFW-Detector"

    @Volatile
    var modelFile: File? = null
        private set


    suspend fun getModelFileInstance(): File {
        return modelFile ?: downloadAndCacheModel().also { modelFile = it }
    }

    private suspend fun downloadAndCacheModel(): File {
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()

        return runCatching {
            FirebaseModelDownloader.getInstance()
                .getModel(MODEL_NAME, DownloadType.LOCAL_MODEL, conditions)
                .await()
        }.map { customModel ->
            customModel.file ?: throw IOException("Downloaded model file is null.")
        }.onSuccess { file ->
            modelFile = file
        }.getOrThrow()
    }
}