package com.example.remotedatasource.client

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.ContentType
import io.ktor.http.contentType


class KtorClient(
    private val interceptor: Interceptor,
){

    private val httpClient = HttpClient() {
        this.withAuthInterceptor(interceptor)
    }

    suspend fun get(url: String): HttpResponse{
        return httpClient.get(url)
    }

    suspend fun post(url: String, body: Any? = null): HttpResponse{
        return httpClient.post(url){
            contentType(ContentType.Application.Json)
            body?.let {
                this.setBody(it)
            }
        }
    }
}
