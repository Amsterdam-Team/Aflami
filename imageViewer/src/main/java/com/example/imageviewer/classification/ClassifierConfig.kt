package com.example.imageviewer.classification


internal object NudityClassifierConfig {
    const val MODEL_PATH = "NSFW.tflite"
    const val LABEL_PATH = "labels.txt"
    val UNSEEN_THRESHOLD = 0.5f

    const val INPUT_IMAGE_WIDTH = 224
    const val INPUT_IMAGE_HEIGHT = 224
    const val OUTPUT_SIZE = 2
}