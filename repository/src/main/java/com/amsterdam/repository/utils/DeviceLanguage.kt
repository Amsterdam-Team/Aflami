package com.amsterdam.repository.utils

import java.util.Locale

fun getDeviceLanguage(): String {
    return when (val language = Locale.getDefault().language.lowercase()) {
        "en", "ar" -> language
        else -> "en"
    }
}