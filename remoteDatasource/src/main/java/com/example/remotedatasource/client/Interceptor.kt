package com.example.remotedatasource.client

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import java.util.Locale

class Interceptor() {
    companion object {
        private const val TOKEN_HEADER_NAME = "api_key"
        private const val LANGUAGE = "language"
        private const val PLUGIN_NAME = "Interceptor"
        private const val SESSION = "session_id"
    }

    fun configure(httpClientConfig: HttpClientConfig<*>) {

        val languageTag = Locale.getDefault().toLanguageTag()

        val token: String? = "839bdb9f035cf0718b6948219ec5c60d"

        val sessionId: String? = null

        httpClientConfig.install(createClientPlugin(PLUGIN_NAME) {
            onRequest { request, _ ->
                if (!token.isNullOrBlank()){
                    request.parameter(TOKEN_HEADER_NAME, token)
                }
                if (!sessionId.isNullOrBlank()){
                    request.parameter(SESSION, sessionId)
                }
                request.parameter(LANGUAGE, languageTag)
            }
        })
    }
}

fun HttpClientConfig<*>.withAuthInterceptor(interceptor: Interceptor) {
    interceptor.configure(this)
}