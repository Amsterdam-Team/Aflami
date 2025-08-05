package com.amsterdam.imageviewer.classification

import android.graphics.Bitmap
import com.amsterdam.imageviewer.classification.model.NsfwDetectorRule

internal interface CustomImageClassifier {

    fun isImageSafe(bitmap: Bitmap, detectorRule: NsfwDetectorRule): Boolean?
}