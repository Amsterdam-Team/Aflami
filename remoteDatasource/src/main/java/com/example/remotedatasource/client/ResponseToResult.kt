package com.example.remotedatasource.client

import com.example.domain.ServerErrorException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

const val SUCCESS_CODE = 200

suspend inline fun <reified T> responseToResult(response: HttpResponse): T =
    when (response.status.value) {
        SUCCESS_CODE -> {
            try {
                response.body<T>()
            } catch (_: Exception) {
                throw ServerErrorException()
            }
        }

        else -> throw ServerErrorException()
    }
