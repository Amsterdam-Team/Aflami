package com.example.remotedatasource.utils

import java.util.Locale

internal fun getDeviceLanguage(): String {
    return Locale.getDefault().language
}