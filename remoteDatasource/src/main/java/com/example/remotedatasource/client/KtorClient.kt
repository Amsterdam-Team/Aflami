package com.example.remotedatasource.client

import com.example.remotedatasource.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.Locale

class KtorClient {
    private val languageTag = Locale.getDefault().toLanguageTag()

    private val token = BuildConfig.BEARER_TOKEN

    private val sessionId: String? = null

    private val httpClient = HttpClient() {
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 30_000
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
        }

        defaultRequest {
            header(TOKEN_HEADER_NAME, "Bearer $token")
            url(BuildConfig.BASE_URL)
            parameters {
                if (!sessionId.isNullOrBlank()) {
                    append(SESSION, sessionId)
                }
                append(LANGUAGE, languageTag)
            }
        }
    }

    suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
        return httpClient.get(url, block)
    }

    suspend fun post(url: String, body: Any? = null): HttpResponse {
        return httpClient.post(url) {
            contentType(ContentType.Application.Json)
            body?.let {
                this.setBody(it)
            }
        }
    }

    companion object {
        private const val TOKEN_HEADER_NAME = "Authorization"
        private const val LANGUAGE = "language"
        private const val SESSION = "session_id"
    }
}
