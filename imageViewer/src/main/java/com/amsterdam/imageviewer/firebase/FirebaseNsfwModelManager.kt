package com.amsterdam.imageviewer.firebase


import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.amsterdam.imageviewer.classification.NsfwDetectorClassifier
import com.amsterdam.imageviewer.classification.createImageClassifier
import com.amsterdam.imageviewer.coil.createImageLoader
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import java.io.File


internal object FirebaseNsfwModelManager {

    private const val MODEL_NAME = "NSFW"

    var downloadState by mutableStateOf<ModelDownloadState>(ModelDownloadState.Idle)
        private set

    fun initialize(context: Context) {
        if (downloadState is ModelDownloadState.Downloading || downloadState is ModelDownloadState.Success) {
            return
        }

        downloadState = ModelDownloadState.Downloading
        val conditions = CustomModelDownloadConditions.Builder().build()
        FirebaseModelDownloader.getInstance()
            .getModel(MODEL_NAME, DownloadType.LOCAL_MODEL_UPDATE_IN_BACKGROUND, conditions)
            .addOnSuccessListener { model ->
                val modelFile = model?.file
                if (modelFile != null) {
                    onDownloadSuccess(context.applicationContext, modelFile)
                } else {
                    onDownloadFailure("Model file not found after download.")
                }
            }
            .addOnFailureListener { exception ->
                onDownloadFailure(exception.message ?: "An unknown network error occurred.")
            }
    }

    private fun onDownloadSuccess(context: Context, modelFile: File) {
        val tfliteClassifier = createImageClassifier(modelFile)
        val nsfwDetector = tfliteClassifier?.let { NsfwDetectorClassifier(it) }
        val imageLoader = createImageLoader(context, nsfwDetector)

        downloadState = if (imageLoader != null) {
            ModelDownloadState.Success(imageLoader)
        } else {
            ModelDownloadState.Error("Failed to create ImageLoader from model file.")
        }
    }

    private fun onDownloadFailure(message: String) {
        downloadState = ModelDownloadState.Error(message)
    }
}