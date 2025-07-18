package com.example.remotedatasource.utils.apiHandler

import android.util.Log
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import com.example.domain.exceptions.UnknownException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.io.IOException
import java.net.ConnectException

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): T {
    val response: HttpResponse
    try {
        response = execute()
    } catch (e: ConnectException) {
        Log.e(
            "safeCall",
            "ConnectException: ${e.message}", e)
        throw NoInternetException()
    } catch (e: SocketTimeoutException) {
        Log.e(
            "safeCall",
            "SocketTimeoutException: ${e.message}", e)
        throw NoInternetException()
    } catch (e: IOException) {
        Log.e(
            "safeCall",
            "IOException: ${e.message}", e)
        throw NoInternetException()
    } catch (e: ClientRequestException) {
        Log.e(
            "safeCall",
            "ClientRequestException: ${e.response.status.value} - ${e.response.bodyAsText()}", e)
        throw ServerErrorException()
    } catch (e: ServerResponseException) {
        Log.e(
            "safeCall",
            "ServerResponseException: ${e.response.status.value} - ${e.response.bodyAsText()}", e)
        throw ServerErrorException()
    } catch (e: Exception) {
        Log.e(
            "safeCall",
            "Unknown Exception: ${e.message}", e)
        throw UnknownException()
    }
    return parseAndHandleHttpStatus(response)
}