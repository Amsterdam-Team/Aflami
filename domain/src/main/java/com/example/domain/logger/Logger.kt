package com.example.domain.logger

interface Logger {
    fun debug(message: Any, tag: String)

    fun info(message: Any, tag: String)

    fun warning(message: Any, tag: String)

    fun error(message: String, tag: String, throwable: Throwable? = null)
}