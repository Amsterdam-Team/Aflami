package com.example.remotedatasource.client

import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import com.example.domain.exceptions.UnknownException
import com.example.remotedatasource.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.util.Locale

class KtorClient(
    private val json: Json
) {
    private val languageTag = Locale.getDefault().toLanguageTag()

    private val token = BuildConfig.BEARER_TOKEN

    private val sessionId: String? = null

    private val httpClient = HttpClient() {
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 30_000
        }

        install(ContentNegotiation) { json(json) }

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

    private suspend fun executeSafely(block: suspend () -> HttpResponse): HttpResponse {
        return try {
            block()
        } catch (e: ConnectException) {
            throw NoInternetException()
        } catch (e: SocketTimeoutException) {
            throw NoInternetException()
        } catch (e: IOException) {
            throw NoInternetException()
        } catch (e: ClientRequestException) {
            throw ServerErrorException()
        } catch (e: ServerResponseException) {
            throw ServerErrorException()
        } catch (e: Exception) {
            throw UnknownException()
        }
    }

    private suspend inline fun <reified T> parseJson(response: HttpResponse): T {
        val responseBody = response.bodyAsText()
        return json.decodeFromString(responseBody)
    }


    internal suspend inline fun <reified T> tryToExecute(
        crossinline block: suspend () -> HttpResponse
    ): T {
        val response = executeSafely { block() }
        return parseJson(response)
    }

    companion object {
        private const val TOKEN_HEADER_NAME = "Authorization"
        private const val LANGUAGE = "language"
        private const val SESSION = "session_id"
    }
}
