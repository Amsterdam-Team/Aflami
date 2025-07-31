package com.amsterdam.ui.utils

import android.annotation.SuppressLint

fun String?.formateAsRate(): String {
    val value = this?.toDoubleOrNull() ?: return ""
    return value.formateAsRate()
}

@SuppressLint("DefaultLocale")
private fun Double.formateAsRate(): String {
    return if (this % 1 == 0.0) {
        toInt().toString()
    } else {
        String.format("%.1f", this)
    }
}

