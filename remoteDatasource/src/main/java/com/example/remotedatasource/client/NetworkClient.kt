package com.example.remotedatasource.client

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse

interface NetworkClient {
    suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun post(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun put(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
    suspend fun delete(url: String, block: HttpRequestBuilder.() -> Unit = {}): HttpResponse
}