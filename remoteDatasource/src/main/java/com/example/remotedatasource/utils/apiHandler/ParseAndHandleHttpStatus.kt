package com.example.remotedatasource.utils.apiHandler

import com.example.domain.exceptions.ServerErrorException
import com.example.domain.exceptions.UnknownException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerializationException

const val SUCCESS_CODE = 200

suspend inline fun <reified T> parseAndHandleHttpStatus(response: HttpResponse): T {
    if (response.status.value != SUCCESS_CODE) {
        throw ServerErrorException()
    }
    return try {
        response.body<T>()
    } catch (e: SerializationException) {
        throw UnknownException()
    } catch (e: Exception) {
        throw UnknownException()
    }
}
