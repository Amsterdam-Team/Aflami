package com.example.imageviewer.classification

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.Tensor
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.concurrent.Volatile
import kotlin.math.exp

internal class NSFWImageClassifier(
    private val context: Context,
    private val modelPath: String = NudityClassifierConfig.MODEL_PATH,
    private val labelPath: String = NudityClassifierConfig.LABEL_PATH
) : ImageClassifier {
    private val unseenThreshold = NudityClassifierConfig.UNSEEN_THRESHOLD
    private val inputWidth = NudityClassifierConfig.INPUT_IMAGE_WIDTH
    private val inputHeight = NudityClassifierConfig.INPUT_IMAGE_HEIGHT
    private val modelOutputSize = NudityClassifierConfig.OUTPUT_SIZE

    private val imageProcessor: ImageProcessor = ImageProcessor.Builder()
        .add(ResizeOp(inputHeight, inputWidth, ResizeOp.ResizeMethod.BILINEAR))
        .build()

    @Volatile
    private var interpreter: Interpreter? = null

    @Volatile
    private var labels: List<String>? = null

    @Volatile
    private var unseenLabelIndex: Int? = null

    @Volatile
    private var inputQuantizationParams: Tensor.QuantizationParams? =
        null

    @Volatile
    private var isSetupAttempted = false

    override suspend fun isImageSafeSuspend(bitmap: Bitmap): Boolean? {
        val setupSuccess = ensureSetup()
        if (!setupSuccess || interpreter == null || unseenLabelIndex == null || inputQuantizationParams == null) {
            return null
        }
        return runInference(interpreter!!, bitmap)
    }

    private suspend fun ensureSetup(): Boolean = withContext(Dispatchers.IO) {
        if (isSetupAttempted && (interpreter == null || labels == null || unseenLabelIndex == null || inputQuantizationParams == null)) {
            return@withContext false
        }
        if (interpreter != null && labels != null && unseenLabelIndex != null && inputQuantizationParams != null) {

            if (inputQuantizationParams == null && interpreter != null) {
                inputQuantizationParams = interpreter!!.getInputTensor(0).quantizationParams()

            }
            return@withContext true
        }

        val interpreterLoaded = setupInterpreterInternal() != null
        val labelsLoaded = loadLabelsInternal() != null

        if (interpreterLoaded && interpreter != null) {
            inputQuantizationParams = try {
                interpreter!!.getInputTensor(0).quantizationParams()
            } catch (e: Exception) {
                null
            }
        } else {
            inputQuantizationParams = null
        }


        isSetupAttempted = true

        val overallSuccess =
            interpreterLoaded && labelsLoaded && unseenLabelIndex != null && inputQuantizationParams != null

        return@withContext overallSuccess
    }


    private suspend fun setupInterpreterInternal(): Interpreter? = withContext(Dispatchers.IO) {
        try {
            val model = FileUtil.loadMappedFile(context, modelPath)
            interpreter = Interpreter(model, Interpreter.Options().apply { setUseNNAPI(true) })
            interpreter
        } catch (e: IOException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun loadLabelsInternal(): List<String>? = withContext(Dispatchers.IO) {
        try {
            val reader = BufferedReader(InputStreamReader(context.assets.open(labelPath)))
            labels = reader.readLines()
            unseenLabelIndex = labels?.indexOf("nude")?.takeIf { it != -1 }
            labels
        } catch (e: IOException) {
            labels = emptyList()
            unseenLabelIndex = null
            null
        } catch (e: Exception) {
            labels = emptyList()
            unseenLabelIndex = null
            null
        }
    }


    private suspend fun runInference(
        interpreter: Interpreter,
        bitmap: Bitmap,
    ): Boolean? = withContext(Dispatchers.IO) {
        try {
            if (bitmap.isRecycled) {
                return@withContext null
            }

            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))
            val inputBuffer = tensorImage.buffer.rewind()

            val outputBuffer =
                TensorBuffer.createFixedSize(intArrayOf(1, modelOutputSize), DataType.FLOAT32)

            interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

            val logits = outputBuffer.floatArray
            val softmaxScores = softmax(logits)

            if (unseenLabelIndex == null) {
                return@withContext null
            }

            val unseenScore = softmaxScores.getOrElse(unseenLabelIndex!!) { 0f }

            if (unseenScore >= unseenThreshold) {
                false
            } else {
                true
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val maxLogit = logits.maxOrNull() ?: 0f
        var sumExp = 0.0f
        val expValues = FloatArray(logits.size)

        for (i in logits.indices) {
            expValues[i] = exp(logits[i] - maxLogit)
            sumExp += expValues[i]
        }

        if (sumExp == 0f) return logits

        val softmaxScores = FloatArray(logits.size)
        for (i in logits.indices) {
            softmaxScores[i] = expValues[i] / sumExp
        }
        return softmaxScores
    }

    override fun close() {
        interpreter?.close()
        interpreter = null
        labels = null
        unseenLabelIndex = null
        inputQuantizationParams = null
        isSetupAttempted = false
    }
}