package com.amsterdam.ui.utils

import android.annotation.SuppressLint


@SuppressLint("DefaultLocale")
fun Double.formatRating(): String {
    return if (this % 1 == 0.0) {
        toInt().toString()
    } else {
        String.format("%.1f", this)
    }
}
fun String?.formatAsRating(): String {
    val value = this?.toDoubleOrNull() ?: return ""
    return value.formatRating()
}

