package com.amsterdam.domain.logger

/**
 * Created by Thoraya.
 * You can use this tag for filtering logs --> AflamiApp
 * If you added a new tag the tag will be like that --> AflamiApp-yourTag
 * Also, you can use your tag direct for filtering logs
 */

object LogManager : Logger {
    private var factory: (() -> Logger?) = { null }

    fun initialize(factory: () -> Logger?) {
        this.factory = factory
    }

    private fun getLogger(): Logger? {
        return factory()
    }

    override fun debug(tag: String, message: Any) {
        getLogger()?.debug(tag, message)
    }

    override fun info(tag: String, message: Any) {
        getLogger()?.info(tag, message)
    }

    override fun warning(tag: String, message: Any) {
        getLogger()?.warning(tag, message)
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        getLogger()?.error(tag, message, throwable)
    }

    override fun errorWithCrashlytics(tag: String, message: String, throwable: Throwable) {
        getLogger()?.errorWithCrashlytics(tag, message, throwable)
    }
}