package com.amsterdam.ui.utils

fun String.withEnglishDigits(): String {
    val arabicToEnglish = mapOf(
        '٠' to '0',
        '١' to '1',
        '٢' to '2',
        '٣' to '3',
        '٤' to '4',
        '٥' to '5',
        '٦' to '6',
        '٧' to '7',
        '٨' to '8',
        '٩' to '9'
    )
    val result = StringBuilder(length)
    for (char in this) {
        result.append(arabicToEnglish[char] ?: char)
    }
    return result.toString()
}

fun String.withEnglishFloat(): String {
    val arabicToEnglishMap = mapOf(
        '٠' to '0', '١' to '1', '٢' to '2', '٣' to '3', '٤' to '4',
        '٥' to '5', '٦' to '6', '٧' to '7', '٨' to '8', '٩' to '9',
        '٫' to '.'
    )
    val result = StringBuilder(length)
    for (char in this) {
        result.append(arabicToEnglishMap[char] ?: char)
    }
    return result.toString()
}