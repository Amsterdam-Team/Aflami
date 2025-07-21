package com.example.remotedatasource.utils.apiHandler

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.ServerErrorException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import kotlinx.serialization.SerializationException

const val SUCCESS_CODE = 200

suspend inline fun <reified T> handleHttpResponse(response: HttpResponse): T {
    if (response.status.value != SUCCESS_CODE) {
        throw ServerErrorException()
    }
    return try {
        response.body<T>()
    } catch (e: SerializationException) {
        throw ServerErrorException()
    } catch (e: Exception) {
        throw NetworkException()
    }
}