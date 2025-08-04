package com.amsterdam.imageviewer.coil


import coil.intercept.Interceptor
import coil.request.ImageResult
import com.amsterdam.imageviewer.classification.CustomImageClassifier
import com.amsterdam.imageviewer.classification.model.NsfwDetectorRule

internal class SafetyInterceptor(
    private val classifier: CustomImageClassifier,
    private val nsfwDetectorRule: NsfwDetectorRule
) : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val safetyTransformation = SafetyBlurTransformation(classifier, detectorRule = nsfwDetectorRule)

        val newRequest = chain.request.newBuilder()
            .transformations(listOf(safetyTransformation) + chain.request.transformations)
            .build()

        return chain.proceed(newRequest)
    }
}