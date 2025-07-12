package com.example.imageviewer.classification.policy

import android.graphics.Bitmap

internal interface ImageClassifier {

    fun isImageSafe(bitmap: Bitmap): Boolean?
}