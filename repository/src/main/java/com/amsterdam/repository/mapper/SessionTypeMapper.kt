package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.SessionType

fun SessionType?.toLocalDto(): String =
    when (this) {
        SessionType.LOGGED_IN -> "LOGGED_IN"
        SessionType.GUEST -> "GUEST"
        else -> ""
    }

fun stringToSessionTypeEntity(value: String): SessionType? {
    return when (value) {
        "LOGGED_IN" -> SessionType.LOGGED_IN
        "GUEST" -> SessionType.GUEST
        else -> null
    }
}