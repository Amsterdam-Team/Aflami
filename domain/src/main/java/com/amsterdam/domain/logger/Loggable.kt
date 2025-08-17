package com.amsterdam.domain.logger

interface Loggable {
    val logger: Logger
        get() = LogManager.getLogger(this::class.simpleName.toString())
}