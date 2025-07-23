package com.example.repository.utils

import java.util.Locale

fun getDeviceLanguage(): String {
    return Locale.getDefault().language
}