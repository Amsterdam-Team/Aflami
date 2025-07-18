package com.example.imageviewer.coil

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import coil.intercept.Interceptor
import coil.request.ErrorResult
import coil.request.ImageResult
import coil.request.SuccessResult
import com.example.imageviewer.classification.ImageClassifier
import coil.size.Size
import coil.transform.Transformation

internal class SafetyInterceptor(
    private val transformation: Transformation
) : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val result = chain.proceed(chain.request)
        if (result is SuccessResult) {
            return try {
                val bitmap = result.drawable.toBitmap()
                val transformedBitmap = transformation.transform(bitmap, Size.ORIGINAL)
                result.copy(
                    drawable = transformedBitmap.toDrawable(chain.request.context.resources)
                )
            } catch (e: Exception) {
                Log.e(TAG, "$ERROR_MSG_SAFETY_CHECK: ${e.message}", e)
                ErrorResult(chain.request.error, chain.request, e)
            }
        }
        return result
    }

    private companion object {
        private const val TAG = "SafeImage"
        private const val ERROR_MSG_SAFETY_CHECK = "Error during bitmap safety check."
    }
}