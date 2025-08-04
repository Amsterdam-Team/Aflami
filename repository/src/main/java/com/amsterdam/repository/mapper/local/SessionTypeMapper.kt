package com.amsterdam.repository.mapper.local

import com.amsterdam.domain.utils.SessionType

fun SessionType.toLocalDto(): String =
    when (this) {
        SessionType.NOT_LOGGED_IN -> "NOT_LOGGED_IN"
        SessionType.LOGGED_IN -> "LOGGED_IN"
        SessionType.GUEST -> "GUEST"
    }

fun String.toSessionTypeEntity(): SessionType =
    when (this) {
        "LOGGED_IN" -> SessionType.LOGGED_IN
        "GUEST" -> SessionType.GUEST
        else -> SessionType.NOT_LOGGED_IN
    }