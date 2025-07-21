package com.example.imageviewer.coil


import coil.intercept.Interceptor
import coil.request.ImageResult
import com.example.imageviewer.classification.ImageClassifier

internal class SafetyInterceptor(
    private val classifier: ImageClassifier
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val safetyTransformation = SafetyBlurTransformation(classifier)

        val newRequest = chain.request.newBuilder()
            .transformations(listOf(safetyTransformation) + chain.request.transformations)
            .build()

        return chain.proceed(newRequest)
    }
}