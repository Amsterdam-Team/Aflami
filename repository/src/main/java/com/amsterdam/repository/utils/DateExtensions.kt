package com.amsterdam.repository.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate

fun String.toSafeLocalDate(): LocalDate? {
    return try {
        this.toLocalDate()
    } catch (_: Exception) {
        null
    }
}