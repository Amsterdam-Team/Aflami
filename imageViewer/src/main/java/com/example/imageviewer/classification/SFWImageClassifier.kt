package com.example.imageviewer.classification

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.imageviewer.firebase.FirebaseModelRepository
import kotlinx.coroutines.runBlocking
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException


internal class SFWImageClassifier(
    private val context: Context,
    modelPath: String = SFWClassifierConfig.NSFW_MODEL_PATH,
    modelFile: java.io.File? = null,
    private val modelRepository: FirebaseModelRepository? = null
) : ImageClassifier {

    private val safetyRules = SFWClassifierConfig.NSFW_SAFETY_RULES

    private val inputWidth = SFWClassifierConfig.INPUT_IMAGE_WIDTH
    private val inputHeight = SFWClassifierConfig.INPUT_IMAGE_HEIGHT
    private val modelOutputSize = 5


    private val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(inputHeight, inputWidth, ResizeOp.ResizeMethod.BILINEAR))
        .add(NormalizeOp(0.0f, 255.0f))
        .build()

    private var interpreter: Interpreter? = run {
        if (modelRepository != null) {
            try {
                val file = runBlocking { modelRepository.ensureModelDownloaded() }
                setupInterpreter(modelPath, file)
            } catch (e: Exception) {
                setupInterpreter(modelPath, modelFile)
            }
        } else {
            setupInterpreter(modelPath, modelFile)
        }
    }

    private fun setupInterpreter(modelPath: String, modelFile: java.io.File?): Interpreter? {
        val options = Interpreter.Options().apply { setUseNNAPI( true) }
        return try {
            if (modelFile != null) {
                Interpreter(modelFile, options)
            } else {
                val modelBuffer = FileUtil.loadMappedFile(context, modelPath)
                Interpreter(modelBuffer, options)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error loading the TFLite model.", e)
            null
        }
    }

    //    private var interpreter: Interpreter? = setupInterpreter(modelPath)
//
//    private fun setupInterpreter(modelPath: String): Interpreter? {
//        return try {
//            val model = FileUtil.loadMappedFile(context, modelPath)
//            Interpreter(model, Interpreter.Options().apply { useNNAPI = true })
//        } catch (e: IOException) {
//            Log.e(TAG, ERROR_MSG_MODEL_LOAD, e)
//            null
//        }
//    }
    override fun isImageSafe(bitmap: Bitmap): Boolean? {
        val scores = runInference(bitmap) ?: return null
        val violatedRuleIndex = findViolatedRuleIndex(scores)
        return if (violatedRuleIndex != null) {
            logUnsafeDecision(violatedRuleIndex, scores)
            false
        } else {
            true
        }
    }

    private fun runInference(bitmap: Bitmap): FloatArray? {
        if (interpreter == null) return null
        return try {
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
            val outputBuffer =
                TensorBuffer.createFixedSize(intArrayOf(1, modelOutputSize), DataType.FLOAT32)
            interpreter?.run(tensorImage.buffer, outputBuffer.buffer.rewind())
            outputBuffer.floatArray
        } catch (e: Exception) {
            Log.e(TAG, ERROR_MSG_INFERENCE, e)
            null
        }
    }

    private fun findViolatedRuleIndex(scores: FloatArray): Int? {
        return safetyRules.firstOrNull { rule ->
            scores.getOrElse(rule.labelIndex) { 0f } >= rule.threshold
        }?.labelIndex
    }

    private fun logUnsafeDecision(violatedRuleIndex: Int, scores: FloatArray) {
        val triggeredRule = safetyRules.first { it.labelIndex == violatedRuleIndex }
        val score = scores[violatedRuleIndex]
        Log.w(
            TAG,
            "Decision: UNSAFE. Rule '${triggeredRule.labelName}' triggered. " +
                    "Score: %.4f, Threshold: %.2f".format(score, triggeredRule.threshold)
        )
    }

    private companion object {
        private const val TAG = "SafeImage"
        private const val ERROR_MSG_MODEL_LOAD = "Error loading TFLite model file."
        private const val ERROR_MSG_INFERENCE = "Error during model inference."
    }


}

