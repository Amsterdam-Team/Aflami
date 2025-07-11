package com.example.remotedatasource.client

import com.example.domain.exceptions.AflamiException
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): T {
    val response = try {
            execute()
        } catch (_: Exception) {
        throw AflamiException()
        }

    return responseToResult(response)
}