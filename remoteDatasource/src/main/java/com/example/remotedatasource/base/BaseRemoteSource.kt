package com.example.remotedatasource.base

import com.example.remotedatasource.client.KtorClient
import io.ktor.client.statement.HttpResponse

abstract class BaseRemoteSource(protected val ktorClient: KtorClient) {

    protected companion object {
        const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w500"
    }

    internal suspend inline fun <reified T> safeExecute(
        crossinline block: suspend () -> HttpResponse
    ): T {
        return ktorClient.tryToExecute(block)
    }
}