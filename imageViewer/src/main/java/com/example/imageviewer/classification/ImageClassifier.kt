package com.example.imageviewer.classification

import android.graphics.Bitmap

internal interface ImageClassifier {
    suspend fun isImageSafeSuspend(bitmap: Bitmap): Boolean?
    fun close()
}