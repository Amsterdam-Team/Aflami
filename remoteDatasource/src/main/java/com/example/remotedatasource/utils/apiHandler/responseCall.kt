package com.example.remotedatasource.utils.apiHandler

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException

suspend inline fun <reified T> responseCall(crossinline execute: suspend () -> T): T {
    return try {
        execute()
    } catch (e: HttpException) {
        throw ServerErrorException()
    } catch (e: SocketTimeoutException) {
        throw NoInternetException()
    } catch (e: ConnectException) {
        throw NoInternetException()
    } catch (e: IOException) {
        throw NoInternetException()
    } catch (e: SerializationException) {
        throw ServerErrorException()
    } catch (e: Exception) {
        throw NetworkException()
    }
}