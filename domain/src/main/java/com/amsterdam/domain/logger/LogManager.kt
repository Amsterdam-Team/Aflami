package com.amsterdam.domain.logger

object LogManager {
    private var factory: (String) -> Logger = { NoLogger() }

    fun initialize(factory: (String) -> Logger) {
        this.factory = factory
    }

    fun getLogger(tag: String): Logger {
        return factory(tag)
    }
}

private class NoLogger : Logger {
    override fun debug(message: Any) {}
    override fun info(message: Any) {}
    override fun warning(message: Any) {}
    override fun error(message: String, throwable: Throwable?) {}
}