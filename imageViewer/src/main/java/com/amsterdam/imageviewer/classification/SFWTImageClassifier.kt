package com.amsterdam.imageviewer.classification

import android.graphics.Bitmap
import com.amsterdam.imageviewer.classification.model.NsfwDetectorRule
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

internal class NsfwDetectorClassifier(
    private val imageClassifier: ImageClassifier
) : CustomImageClassifier {

    override fun isImageSafe(bitmap: Bitmap, detectorRule: NsfwDetectorRule): Boolean? {
        return runInference(bitmap, detectorRule)?.let { scores ->
            isInappropriate(scores, detectorRule).not()
        }
    }

    private fun runInference(bitmap: Bitmap, detectorRule: NsfwDetectorRule): FloatArray? {
        return synchronized(this) {
            runCatching {
                val tensorImage = TensorImage.fromBitmap(bitmap)

                val results = imageClassifier.classify(tensorImage)

                val scores = FloatArray(2)
                results.firstOrNull()?.categories?.forEach { category ->
                    when (category.label) {
                        "0" -> scores[detectorRule.nonNudeIndex] = category.score
                        "1" -> scores[detectorRule.nudeIndex] = category.score
                    }
                }
                scores
            }.getOrNull()
        }
    }

    private fun isInappropriate(scores: FloatArray, detectorRule: NsfwDetectorRule): Boolean {
        val nonNudeScore = scores.getOrElse(detectorRule.nonNudeIndex) { 0f }
        val nudeScore = scores.getOrElse(detectorRule.nudeIndex) { 0f }
        return nonNudeScore < detectorRule.sfwThreshold || nudeScore >= detectorRule.nsfwThreshold
    }
}