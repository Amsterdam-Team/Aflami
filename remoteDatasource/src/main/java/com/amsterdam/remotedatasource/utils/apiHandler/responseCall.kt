package com.amsterdam.remotedatasource.utils.apiHandler

import com.amsterdam.domain.exceptions.AccessDeniedException
import com.amsterdam.domain.exceptions.AccessRestrictedException
import com.amsterdam.domain.exceptions.AccountDisabledException
import com.amsterdam.domain.exceptions.InvalidCredentialsException
import com.amsterdam.domain.exceptions.InvalidSessionException
import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.domain.exceptions.NoInternetException
import com.amsterdam.domain.exceptions.ServerErrorException
import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.exceptions.VerificationRequiredException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException

suspend inline fun <reified T> responseCall(crossinline execute: suspend () -> T): T =
    try {
        execute()
    } catch (e: HttpException) {
        e.printStackTrace()
        throw ServerErrorException()
    } catch (e: ConnectException) {
        throw NoInternetException()
    } catch (e: SerializationException) {
        throw ServerErrorException()
    } catch (e: Exception) {
        throw NetworkException()
    }

suspend inline fun <reified T> responseCall(
    crossinline execute: suspend () -> T,
    onHttpError: (String) -> Int,
): T =
    try {
        execute()
    } catch (e: HttpException) {
        throw if (e.code() == 401) {
            val serverResponse = e.response()?.errorBody()?.string() ?: throw UnknownException()
            mapHttpCodeToDomainException(onHttpError(serverResponse))
        } else {
            ServerErrorException()
        }
    } catch (e: ConnectException) {
        throw NoInternetException()
    } catch (e: SerializationException) {
        throw ServerErrorException()
    } catch (e: Exception) {
        throw NetworkException()
    }

fun mapHttpCodeToDomainException(code: Int): NetworkException =
    when (code) {
        3, 14, 30 -> InvalidCredentialsException()
        7, 10, 16, 17, 33, 35, 36 -> InvalidSessionException()
        31 -> AccountDisabledException()
        32 -> VerificationRequiredException()
        38, 39 -> AccessDeniedException()
        45 -> AccessRestrictedException()
        else -> NetworkException()
    }
