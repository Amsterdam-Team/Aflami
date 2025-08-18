package com.amsterdam.domain.logger

interface Logger {
    fun debug(tag: String, message: Any)
    fun info(tag: String, message: Any)
    fun warning(tag: String, message: Any)
    fun error(tag: String, message: String, throwable: Throwable?)
    fun errorWithCrashlytics(tag: String, message: String, throwable: Throwable)
}