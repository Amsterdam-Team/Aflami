package com.amsterdam.viewmodel.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

fun LocalDate?.toShortMonthString(language: String): String {
    return this?.let {
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale(language))
        it.toJavaLocalDate().format(formatter)
    } ?: ""


}