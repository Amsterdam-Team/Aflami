package com.amsterdam.repository.mapper

import com.amsterdam.domain.utils.SessionType

fun SessionType.toLocalDto(): String =
    when (this) {
        SessionType.NOT_LOGGED_IN -> "NOT_LOGGED_IN"
        SessionType.LOGGED_IN -> "LOGGED_IN"
        SessionType.GUEST -> "GUEST"
    }

fun stringToSessionTypeEntity(value: String): SessionType {
    return when (value) {
        "LOGGED_IN" -> SessionType.LOGGED_IN
        "GUEST" -> SessionType.GUEST
        else -> SessionType.NOT_LOGGED_IN
    }
}