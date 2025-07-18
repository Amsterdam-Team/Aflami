package com.example.imageviewer.classification

import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class SFWImageClassifier(
    private val interpreter: Interpreter
) : ImageClassifier {
    private val safetyRules = SFWClassifierConfig.NSFW_SAFETY_RULES

    private val inputWidth = SFWClassifierConfig.INPUT_IMAGE_WIDTH
    private val inputHeight = SFWClassifierConfig.INPUT_IMAGE_HEIGHT
    private val modelOutputSize = 5


    private val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(inputHeight, inputWidth, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(0.0f, 255.0f))
        .build()


    override fun isImageSafe(bitmap: Bitmap): Boolean? {
        return runInference(bitmap)?.let { scores -> findViolatedRule(scores) == null }
    }


    private fun runInference(bitmap: Bitmap): FloatArray? {
        return runCatching {
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

            val outputBuffer =
                TensorBuffer.createFixedSize(intArrayOf(1, modelOutputSize), DataType.FLOAT32)

            interpreter.run(tensorImage.buffer, outputBuffer.buffer.rewind())

            outputBuffer.floatArray
        }.getOrNull()
    }

    private fun findViolatedRule(scores: FloatArray): Int? {
        return safetyRules.firstOrNull { rule ->
            val score = scores.getOrElse(rule.labelIndex) { 0f }
            score >= rule.threshold
        }?.labelIndex
    }
}