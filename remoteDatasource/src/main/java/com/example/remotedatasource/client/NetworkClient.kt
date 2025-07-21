package com.example.remotedatasource.client

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse

interface NetworkClient {
    suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
}