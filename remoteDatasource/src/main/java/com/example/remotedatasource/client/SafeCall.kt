package com.example.remotedatasource.client

import com.example.domain.NoInternetException
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): T {
    val response = try {
            execute()
        } catch (_: Exception) {
            throw NoInternetException()
        }

    return responseToResult(response)
}
