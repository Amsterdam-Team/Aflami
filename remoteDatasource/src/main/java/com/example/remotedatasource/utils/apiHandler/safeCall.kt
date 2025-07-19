package com.example.remotedatasource.utils.apiHandler

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import kotlinx.io.IOException
import java.net.ConnectException

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): T {
    val response: HttpResponse
    try {
        response = execute()
    } catch (e: ConnectException) {
        throw NoInternetException()
    } catch (e: SocketTimeoutException) {
        throw NoInternetException()
    } catch (e: IOException) {
        throw NoInternetException()
    } catch (e: ClientRequestException) {
        throw ServerErrorException()
    } catch (e: ServerResponseException) {
        throw ServerErrorException()
    } catch (e: Exception) {
        throw NetworkException()
    }
    return handleHttpResponse(response)
}