package com.example.domain.logger

interface Logger {
    fun d(message: Any, tag: String)

    fun i(message: Any, tag: String)

    fun w(message: Any, tag: String)

    fun e(message: String, tag: String, throwable: Throwable? = null)
}