package com.example.imageviewer.coil


import android.graphics.Bitmap
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import coil.intercept.Interceptor
import coil.request.ErrorResult
import coil.request.ImageResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import coil.request.SuccessResult
import com.example.imageviewer.classification.ImageClassifier
import com.example.imageviewer.util.OpenGLBlurProcessor

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
                        val blurredBitmap = applyBlur( bitmap, BLUR_RADIUS)

                        if (blurredBitmap != null && !blurredBitmap.isRecycled) {
                            return result.copy(
                                drawable = blurredBitmap.toDrawable(chain.request.context.resources)
                            )
                        } else {
                            return ErrorResult(
                                chain.request.error,
                                chain.request,
                                RuntimeException(
                                    "Failed to blur image after safety check",
                                    IllegalStateException("Blur failed")
                                )
                            )
                        }
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

    private suspend fun applyBlur( source: Bitmap, radius: Float): Bitmap? {
        return withContext(Dispatchers.IO) {
            if (radius <= 0f) {
                return@withContext source
            }
            if (source.isRecycled) {
                return@withContext null
            }

            val clampedRadius = radius.coerceIn(0.0f, 25.0f)
            if (clampedRadius == 0.0f) {
                return@withContext source
            }
            val openGlInputBitmap = try {
                if (source.config == Bitmap.Config.ARGB_8888 && !source.isMutable) {
                    source
                } else {
                    source.copy(Bitmap.Config.ARGB_8888, false)
                }
            } catch (e: Exception) {
                null
            }

            if (openGlInputBitmap == null) {
                return@withContext null
            }


            val blurredBitmap = try {
                OpenGLBlurProcessor.blurBitmap(openGlInputBitmap, clampedRadius)
            } catch (e: Exception) {
                null
            } finally {
                if (openGlInputBitmap != source && !openGlInputBitmap.isRecycled) {
                    openGlInputBitmap.recycle()
                }
            }

            return@withContext blurredBitmap
        }
    }
    private companion object {
        private const val TAG = "SafeImage"
        private const val ERROR_MSG_SAFETY_CHECK = "Error during bitmap safety check."
        private const val DOWNSCALE_FACTOR = 10
        private const val BLUR_RADIUS = 25f
    }
}