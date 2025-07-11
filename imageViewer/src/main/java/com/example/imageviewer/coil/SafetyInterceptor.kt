package com.example.imageviewer.coil

import com.example.imageviewer.classification.ImageClassifier
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import coil.request.ErrorResult
import coil.request.ImageResult
import coil.request.SuccessResult
import coil.intercept.Interceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.imageviewer.util.OpenGLBlurProcessor


internal class SafetyInterceptor(
    private val classifier: ImageClassifier?
) : Interceptor {

    private suspend fun applyBlur(context: Context, source: Bitmap, radius: Float): Bitmap? {
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


    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val result = try {
            chain.proceed(chain.request)
        } catch (e: Exception) {
            return ErrorResult(chain.request.error, chain.request, e)
        }

        if (result is SuccessResult && classifier != null) {
            return try {
                val originalBitmap = (result.drawable as? BitmapDrawable)?.bitmap
                    ?: result.drawable.toBitmap()

                val classifierInputBitmap = if (originalBitmap.config == Bitmap.Config.ARGB_8888) {
                    originalBitmap
                } else {
                    withContext(Dispatchers.IO) {
                        try {
                            originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
                        } catch (e: Exception) {
                            null
                        }
                    }
                }

                if (classifierInputBitmap == null) {
                    return ErrorResult(
                        chain.request.error,
                        chain.request,
                        RuntimeException(
                            "Bitmap format error for classification",
                            IllegalStateException("Failed to create ARGB_8888 copy")
                        )
                    )
                }

                val safetyResult = classifier.isImageSafeSuspend(classifierInputBitmap)

                when (safetyResult) {
                    true -> {
                        result
                    }

                    false -> {
                        val blurredBitmap = applyBlur(chain.request.context, originalBitmap, BLUR_RADIUS)

                        if (blurredBitmap != null && !blurredBitmap.isRecycled) {
                            result.copy(
                                drawable = blurredBitmap.toDrawable(chain.request.context.resources)
                            )
                        } else {
                            ErrorResult(
                                chain.request.error,
                                chain.request,
                                RuntimeException(
                                    "Failed to blur image after safety check",
                                    IllegalStateException("Blur failed")
                                )
                            )
                        }
                    }

                    null -> {
                        ErrorResult(
                            chain.request.error,
                            chain.request,
                            RuntimeException(
                                "Image safety check failed",
                                IllegalStateException("Classifier failure")
                            )
                        )
                    }
                }

            } catch (e: Exception) {
                ErrorResult(
                    chain.request.error,
                    chain.request,
                    RuntimeException("Image processing error", e)
                )
            }
        }
        return result
    }

    private companion object {
        private const val BLUR_RADIUS = 15f
    }
}