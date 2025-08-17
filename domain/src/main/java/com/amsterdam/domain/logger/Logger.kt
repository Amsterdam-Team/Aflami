package com.amsterdam.domain.logger

interface Logger {
    fun debug(message: Any)
    fun info(message: Any)
    fun warning(message: Any)
    fun error(message: String, throwable: Throwable? = null)
}