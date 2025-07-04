package com.example.imageviewer.classification

import android.graphics.Bitmap

internal interface ImageClassifier {

    fun isImageSafe(bitmap: Bitmap): Boolean?
}