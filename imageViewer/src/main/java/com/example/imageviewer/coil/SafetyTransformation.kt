package com.example.imageviewer.coil

import android.graphics.Bitmap
import android.util.Log
import coil.size.Size
import coil.transform.Transformation
import com.example.imageviewer.classification.ImageClassifier
import com.example.imageviewer.util.OpenGLBlurProcessor

internal class SafetyTransformation(
    private val classifier: ImageClassifier
) : Transformation {
    override val cacheKey: String = "SafetyTransformation"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        return try {
            val modelInputBitmap = if (input.config == Bitmap.Config.ARGB_8888) {
                input
            } else {
                input.copy(Bitmap.Config.ARGB_8888, false)
            }
            val isSafe = classifier.isImageSafe(modelInputBitmap) ?: true
            if (isSafe) {
                input
            } else {
                val blurredBitmap = applyBlur(input, BLUR_RADIUS)
                blurredBitmap ?: input
            }
        } catch (e: Exception) {
            Log.e(TAG, ERROR_MSG_SAFETY_CHECK, e)
            input
        }
    }

    private fun applyBlur(source: Bitmap, radius: Float): Bitmap? {
        if (radius <= 0f) return source
        if (source.isRecycled) return null
        val clampedRadius = radius.coerceIn(0.0f, 25.0f)
        if (clampedRadius == 0.0f) return source
        val openGlInputBitmap = try {
            if (source.config == Bitmap.Config.ARGB_8888 && !source.isMutable) {
                source
            } else {
                source.copy(Bitmap.Config.ARGB_8888, false)
            }
        } catch (e: Exception) {
            null
        }
        if (openGlInputBitmap == null) return null
        val blurredBitmap = try {
            OpenGLBlurProcessor.blurBitmap(openGlInputBitmap, clampedRadius)
        } catch (e: Exception) {
            null
        } finally {
            if (openGlInputBitmap != source && !openGlInputBitmap.isRecycled) {
                openGlInputBitmap.recycle()
            }
        }
        return blurredBitmap
    }

    private companion object {
        private const val TAG = "SafeImage"
        private const val ERROR_MSG_SAFETY_CHECK = "Error during bitmap safety check."
        private const val BLUR_RADIUS = 25f
    }
} 