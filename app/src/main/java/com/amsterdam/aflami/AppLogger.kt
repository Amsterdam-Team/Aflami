package com.amsterdam.aflami

import android.util.Log
import com.amsterdam.domain.logger.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics

class AppLogger(
    private val tag: String,
    private val isLoggerEnabled: Boolean = BuildConfig.DEBUG
) : Logger {

    companion object {
        const val TAG_PREFIX = "AflamiApp"
        const val MAX_LOG_LENGTH = 4000
        const val MAX_TAG_LENGTH = 23
    }

    private val callerInfo: String
        get() = getDetailedSourceInfo()

    override fun debug(message: Any) {
        log(Log.DEBUG, "🐛", message.toString())
    }

    override fun info(message: Any) {
        log(Log.INFO, "ℹ️", message.toString())
    }

    override fun warning(message: Any) {
        log(Log.WARN, "⚠️", message.toString())
    }

    override fun error(message: String, throwable: Throwable?) {
        log(Log.ERROR, "🔥", message, throwable)
        recordTheProblemInFirebaseCrashlytics(throwable, message)
    }

    private fun recordTheProblemInFirebaseCrashlytics(throwable: Throwable?, message: String) {
        if (throwable != null) {
            val crashlytics = FirebaseCrashlytics.getInstance()
            crashlytics.log(message)
            crashlytics.recordException(throwable)
        }
    }

    private fun getDetailedSourceInfo(): String {
        val stackTrace = Thread.currentThread().stackTrace
        val loggerClassName = AppLogger::class.java.name
        val startIndex = 5

        for (i in startIndex until stackTrace.size) {
            val element = stackTrace[i]
            val className = element.className
            if (className.startsWith(loggerClassName) ||
                className.startsWith("kotlinx.coroutines.") ||
                className.startsWith("java.lang.")
            ) {
                continue
            }
            val simpleClassName = className.substringAfterLast('.')
            return "($simpleClassName.kt:${element.lineNumber}) - ${element.methodName}()"
        }

        return "(Unknown Source)"
    }

    private fun log(
        priority: Int,
        emoji: String,
        message: String,
        throwable: Throwable? = null
    ) {
        if (!isLoggerEnabled) return

        val maxClassNameLength = MAX_TAG_LENGTH - (TAG_PREFIX.length + 1)
        val className = this.tag
        val shortenedClassName = className.take(maxClassNameLength)
        val finalTag = "$TAG_PREFIX-$shortenedClassName"
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