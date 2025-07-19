package com.amsterdam.blurred.blurProcessor

import android.graphics.Bitmap

internal interface BlurProcessor {

    fun gaussianBlur(inputBitmap: Bitmap, radius: Float, blurEdgeTreatment: BlurEdgeTreatment): Bitmap

}