package com.amsterdam.viewmodel.utils

import kotlinx.datetime.LocalDate

fun LocalDate?.toFormattedString(): String {
    return this?.let { date ->
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.monthNumber.toString().padStart(2, '0')
        val year = date.year.toString()
        "$day-$month-$year"
    } ?: ""
}
fun LocalDate?.toYearString(): String {
    return this?.year?.toString() ?: ""
}
fun movieLengthToHourMinuteString(movieLength: Int): String {
    val hours = movieLength / 60
    val minutes = movieLength % 60
    return "${hours}h ${minutes}m"
}

fun formatDuration(duration: Int): String {
    val hours = duration / 60
    val minutes = duration % 60

    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}