package com.amsterdam.aflami

import android.util.Log
import com.amsterdam.domain.logger.LogManager
import com.amsterdam.domain.logger.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Created by Thoraya.
 */
class AppLogger(
    private val isLoggerEnabled: Boolean = BuildConfig.DEBUG
) : Logger {

    companion object {
        const val TAG_PREFIX = "AflamiApp"
        const val MAX_LOG_LENGTH = 4000
    }

    override fun debug(tag: String, message: Any) {
        log(Log.DEBUG, tag, "🐛", message.toString())
    }

    override fun info(tag: String, message: Any) {
        log(Log.INFO, tag, "ℹ️", message.toString())
    }

    override fun warning(tag: String, message: Any) {
        log(Log.WARN, tag, "⚠️", message.toString())
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        log(Log.ERROR, tag, "🔥", message, throwable = throwable ?: RuntimeException(message))
    }

    override fun errorWithCrashlytics(tag: String, message: String, throwable: Throwable) {
        recordTheProblemInFirebaseCrashlytics(throwable, message)
        log(Log.ERROR, tag, "🔥", message, throwable)
    }

    private fun log(
        priority: Int,
        tag: String,
        emoji: String,
        message: String,
        throwable: Throwable? = null
    ) {
        if (!isLoggerEnabled) return

        val callerFinder = RuntimeException(message)
        val caller = findCallerStackTraceElement(callerFinder.stackTrace)

        val callerInfo = getCallerInfo(caller)

        val finalTag = "$TAG_PREFIX-$tag"
        val threadInfo = "[${Thread.currentThread().name}]"
        val header = "$emoji $threadInfo $callerInfo"

        var fullMessage = "$header\n$message\n"
        if (throwable != null) {
            fullMessage = "$header\n$message\n${Log.getStackTraceString(throwable)}"
        }

        if (fullMessage.length > MAX_LOG_LENGTH) {
            logChunked(priority, finalTag, fullMessage)
        } else {
            Log.println(priority, finalTag, fullMessage)
        }
    }

    private fun logChunked(priority: Int, finalTag: String, fullMessage: String) {
        var i = 0
        while (i < fullMessage.length) {
            val end = (i + MAX_LOG_LENGTH).coerceAtMost(fullMessage.length)
            Log.println(priority, finalTag, fullMessage.substring(i, end))
            i = end
        }
    }

    private fun getCallerInfo(caller: StackTraceElement?): String {
        val callerInfo = if (caller != null) {
            val simpleClassName = caller.className.substringAfterLast('.')
            "($simpleClassName.kt:${caller.lineNumber}) - ${caller.methodName}()"
        } else {
            "(Unknown Source)"
        }
        return callerInfo
    }

    private fun findCallerStackTraceElement(stackTrace: Array<StackTraceElement>): StackTraceElement? {
        val ignoredClassNames = setOf(
            AppLogger::class.java.name,
            LogManager::class.java.name,
            Logger::class.java.name,
        )

        val ignoredHelperClassNames = setOf(
            "com.amsterdam.remotedatasource.utils.apiHandler.responseCallKt",
        )

        val allIgnoredClassNames = ignoredClassNames + ignoredHelperClassNames
        return stackTrace.firstOrNull {
            println(it.className)
            it.className !in allIgnoredClassNames
        }
    }

    private fun recordTheProblemInFirebaseCrashlytics(throwable: Throwable, message: String) {
        val crashlytics = FirebaseCrashlytics.getInstance()
        crashlytics.log(message)
        crashlytics.recordException(throwable)
    }
}