package com.example.imageviewer.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.suspendCancellableCoroutine
import org.tensorflow.lite.Interpreter
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

interface FirebaseModelRepository {
    suspend fun ensureModelDownloaded(): File
    suspend fun getInterpreter(): Interpreter
}

object FirebaseModelManager : FirebaseModelRepository {
    private const val MODEL_NAME = "NSFW-Detector"
    @Volatile
    private var modelFile: File? = null
    @Volatile
    private var interpreter: Interpreter? = null
    private val modelReady = CompletableDeferred<File>()

    override suspend fun ensureModelDownloaded(): File {
        Log.d("firebaseImg","in ensureModelDownloaded")
        if (modelFile != null) return modelFile!!
        return try {
            val file = downloadModel()
            if (!modelReady.isCompleted) modelReady.complete(file)
            file
        } catch (e: Exception) {
            if (!modelReady.isCompleted) modelReady.completeExceptionally(e)
            throw e
        }
    }

     suspend fun downloadModel(): File = suspendCancellableCoroutine { cont ->
         Log.d("firebaseImg","in ensureModelDownloaded :: try downloadModel")
        val conditions = CustomModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        FirebaseModelDownloader.getInstance()
            .getModel(MODEL_NAME, DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model: CustomModel? ->
                val file = model?.file
                if (file != null) {
                    modelFile = file
                    cont.resume(file)
                } else {
                    Log.d("firebaseImg","in ensureModelDownloaded :: try downloadModel :: else Model file is null")

                    cont.resumeWithException(IllegalStateException("Model file is null"))
                }
                Log.d("firebaseImg","downloadModel ${model?.file?.path}")
            }
            .addOnFailureListener { e ->

                Log.d("firebaseImg","in ensureModelDownloaded :: try downloadModel :: failed ${e.printStackTrace()}")
                cont.resumeWithException(e)
            }
    }

    override suspend fun getInterpreter(): Interpreter {
        if (interpreter != null) return interpreter!!
        return Interpreter(modelFile!!).also { interpreter = it }
    }
}

