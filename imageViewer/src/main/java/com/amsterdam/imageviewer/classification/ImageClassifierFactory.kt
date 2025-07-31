package com.amsterdam.imageviewer.classification


import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.File

internal fun createImageClassifier(modelFile: File): ImageClassifier? {
    return try {
        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setMaxResults(2)
            .build()
        ImageClassifier.createFromFileAndOptions(modelFile, options)
    } catch (e: Exception) {
        null
    }
}