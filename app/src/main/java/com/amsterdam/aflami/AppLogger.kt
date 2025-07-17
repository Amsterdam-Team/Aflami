package com.amsterdam.aflami

import android.util.Log
import com.example.domain.logger.Logger
import com.example.remotedatasource.BuildConfig

class AppLogger : Logger {

    companion object {
        const val TAG_PREFIX = "AflamiApp"
        const val MAX_LOG_LENGTH = 4000
    }

    private var isLoggerEnabled = BuildConfig.DEBUG

    private val callerInfo: String
        get() {
            return getLoggerInfo()
        }

    override fun d(message: Any, tag: String) {
        log(Log.DEBUG, "🐛", message.toString(), tag)
    }

    override fun i(message: Any, tag: String) {
        log(Log.INFO, "ℹ️", message.toString(), tag)
    }

    override fun w(message: Any, tag: String) {
        log(Log.WARN, "⚠️", message.toString(), tag)
    }

    override fun e(message: String, tag: String, throwable: Throwable?) {
        log(Log.ERROR, "🔥", message, tag, throwable)
    }

    private fun getLoggerInfo(): String {
        val stackTrace = Thread.currentThread().stackTrace
        val loggerClassName = AppLogger::class.java.name
        var foundLogger = false
        for (element in stackTrace) {
            if (element.className.startsWith(loggerClassName)) {
                foundLogger = true
                continue
            }
            if (foundLogger) {
                val className = element.className.substringAfterLast('.')
                return "($className.kt:${element.lineNumber}) - ${element.methodName}()"
            }
        }
        return "(Unknown Source)"
    }

    private fun generateTag(customTag: String?): String {
        val callerClassName = Thread.currentThread().stackTrace
            .getOrNull(4)
            ?.className
            ?.substringAfterLast('.') ?: "App"

        return if (customTag != null) {
            "$TAG_PREFIX-$customTag"
        } else {
            "$TAG_PREFIX-$callerClassName"
        }
    }

    private fun log(
        priority: Int,
        emoji: String,
        message: String,
        tag: String?,
        throwable: Throwable? = null
    ) {
        if (!isLoggerEnabled) return

        val finalTag = generateTag(tag)
        val threadInfo = "[${Thread.currentThread().name}]"
        val header = "$emoji $threadInfo $callerInfo"

        if (throwable != null) {
            Log.println(
                priority,
                finalTag,
                "$header\n$message\n${Log.getStackTraceString(throwable)}"
            )
            return
        }
        if (message.length > MAX_LOG_LENGTH) {
            logChunked(priority, finalTag, header, message)
        } else {
            Log.println(priority, finalTag, "$header\n$message")
        }
    }

    private fun logChunked(priority: Int, tag: String, header: String, message: String) {
        Log.println(priority, tag, "$header (part 1)")
        var i = 0
        while (i < message.length) {
            val end = (i + MAX_LOG_LENGTH).coerceAtMost(message.length)
            Log.println(priority, tag, message.substring(i, end))
            i = end
        }
    }
}