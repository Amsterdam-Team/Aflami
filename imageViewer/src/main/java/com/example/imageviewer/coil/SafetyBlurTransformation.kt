package com.example.imageviewer.coil

import android.graphics.Bitmap
import android.util.Log
import coil.size.Size
import coil.transform.Transformation
import com.example.imageviewer.classification.ImageClassifier
import com.example.imageviewer.util.OpenGLBlurProcessor

internal class SafetyBlurTransformation(
    private val classifier: ImageClassifier,
    private val blurRadius: Float = 25f,
) : Transformation {

    override val cacheKey: String = "SafetyBlurTransformation(radius=${blurRadius})"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val isSafe = checkBitmapSafety(input)

        return if (isSafe) {
            input.copy(input.config!!, true)
        } else {
            applyBlur(input, blurRadius) ?: throw RuntimeException("Failed to apply blur to unsafe image.")
        }
    }


    private fun checkBitmapSafety(bitmap: Bitmap): Boolean {
        val modelInputBitmap = if (bitmap.config == Bitmap.Config.ARGB_8888) {
            bitmap
        } else {
            bitmap.copy(Bitmap.Config.ARGB_8888, false)
        }

        return classifier.isImageSafe(modelInputBitmap) ?: true
    }

    private fun applyBlur(source: Bitmap, radius: Float): Bitmap? {
        if (radius <= 0f) return source.config?.let { source.copy(it, true) }
        if (source.isRecycled) return null

        val clampedRadius = radius.coerceIn(0.0f, 25.0f)
        if (clampedRadius == 0.0f) return source.config?.let { source.copy(it, true) }

        val openGlInputBitmap = try {
            if (source.config == Bitmap.Config.ARGB_8888 && !source.isMutable) {
                source
            } else {
                source.copy(Bitmap.Config.ARGB_8888, false)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Could not copy bitmap for blurring.", e)
            null
        } ?: return null

        return try {
            OpenGLBlurProcessor.blurBitmap(openGlInputBitmap, clampedRadius)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to execute OpenGL blur.", e)
            null
        } finally {
            if (openGlInputBitmap != source && !openGlInputBitmap.isRecycled) {
                openGlInputBitmap.recycle()
            }
        }
    }

    private companion object {
        private const val TAG = "SafetyBlur"
    }
}