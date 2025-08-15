package com.amsterdam.ui.utils

import java.util.Locale

fun Any?.toLocalizedNumbers(): String {
    val currentLocale = Locale.getDefault()
    var text = ""
    runCatching {
        text = this.toString()
        return if (currentLocale.language == "ar") {
            text.map { char ->
                when (char) {
                    '0' -> '٠'
                    '1' -> '١'
                    '2' -> '٢'
                    '3' -> '٣'
                    '4' -> '٤'
                    '5' -> '٥'
                    '6' -> '٦'
                    '7' -> '٧'
                    '8' -> '٨'
                    '9' -> '٩'
                    else -> char
                }
            }.joinToString("")
        } else {
            text
        }
    }
    return text
}

fun getLocalizedTimeUnit(unit: String): String {
    val lang = Locale.getDefault().language
    val arMap = mapOf("h" to "س", "m" to "د")
    val enMap = mapOf("h" to "h", "m" to "m")

    val key = unit.lowercase()
    return when (lang) {
        "ar" -> arMap[key] ?: unit
        else -> enMap[key] ?: unit
    }
}

fun reverseDateFormat(input: String): String {
    var day = ""
    var month = ""
    var year = ""
    runCatching {
        val parts = input.split("-")
        day = parts[2]
        month = parts[1]
        year = parts[0]
    }
    return "$year-$month-$day".toLocalizedNumbers()
}

fun formatMovieLength(length: String): String {
    val trimmed = length.trim()
    val hours: Int?
    val minutes: Int?

    if (trimmed.contains("h") || trimmed.contains("m")) {
        hours = Regex("(\\d+)h").find(trimmed)?.groupValues?.get(1)?.toIntOrNull()
        minutes = Regex("(\\d+)m").find(trimmed)?.groupValues?.get(1)?.toIntOrNull()
    } else {
        val totalMinutes = trimmed.toIntOrNull()
        hours = totalMinutes?.div(60)
        minutes = totalMinutes?.rem(60)
    }

    return buildString {
        if (hours != null && hours > 0) {
            append("${hours.toLocalizedNumbers()}${getLocalizedTimeUnit("h")} ")
        }
        if (minutes != null && minutes > 0) {
            append("${minutes.toLocalizedNumbers()}${getLocalizedTimeUnit("m")}")
        }
    }.trim()
}

fun localizeCountryCode(countryCode: String): String {
    val lang = Locale.getDefault().language
    return if (lang == "ar") {
        Locale(lang, countryCode).getDisplayCountry(Locale(lang)).toLocalizedNumbers()
    } else {
        countryCode.uppercase()
    }
}

