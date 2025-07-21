package com.example.remotedatasource.utils.apiHandler

import com.example.domain.exceptions.NetworkException
import com.example.domain.exceptions.NoInternetException
import com.example.domain.exceptions.ServerErrorException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException

suspend inline fun <reified T> responseCall(crossinline execute: suspend () -> T): T {
    return try {
        execute()
    } catch (e: HttpException) {
        throw ServerErrorException()
    } catch (e: ConnectException) {
        throw NoInternetException()
    } catch (e: SerializationException) {
        throw ServerErrorException()
    } catch (e: Exception) {
        throw NetworkException()
    }
}