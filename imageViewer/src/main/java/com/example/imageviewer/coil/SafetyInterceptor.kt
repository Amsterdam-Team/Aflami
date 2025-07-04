package com.example.imageviewer.coil

import com.example.imageviewer.classification.ImageClassifier

import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.scale
import coil.intercept.Interceptor
import coil.request.ErrorResult
import coil.request.ImageResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import coil.request.SuccessResult

internal class SafetyInterceptor(
    private val classifier: ImageClassifier
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val result = chain.proceed(chain.request)

        if (result is SuccessResult) {
            return try {
                val bitmap = result.drawable.toBitmap()
                val isSafe = checkBitmapSafety(bitmap)
                when (isSafe) {
                    true -> {
                        result
                    }

                    false -> {
                        val blurredBitmap = createBlurredBitmap(bitmap)
                        return result.copy(
                            drawable = blurredBitmap.toDrawable(chain.request.context.resources)
                        )
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "$ERROR_MSG_SAFETY_CHECK: ${e.message}", e)
                return ErrorResult(chain.request.error, chain.request, e)
            }
        }
        return result
    }

    private suspend fun checkBitmapSafety(bitmap: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val modelInputBitmap = if (bitmap.config == Bitmap.Config.ARGB_8888) {
                    bitmap
                } else {
                    bitmap.copy(Bitmap.Config.ARGB_8888, false)
                }
                classifier.isImageSafe(modelInputBitmap) ?: true
            } catch (e: Exception) {
                Log.e(TAG, ERROR_MSG_SAFETY_CHECK, e)
                true
            }
        }
    }

    private fun createBlurredBitmap(source: Bitmap): Bitmap {
        val downscaleFactor = DOWNSCALE_FACTOR
        val originalWidth = source.width
        val originalHeight = source.height

        if (originalWidth < downscaleFactor || originalHeight < downscaleFactor) return source

        return source
            .scale(originalWidth / downscaleFactor, originalHeight / downscaleFactor, false)
            .scale(originalWidth, originalHeight, false)
    }

    private companion object {
        private const val TAG = "SafetyInterceptor"
        private const val ERROR_MSG_SAFETY_CHECK = "Error during bitmap safety check."
        private const val DOWNSCALE_FACTOR = 10
    }
}