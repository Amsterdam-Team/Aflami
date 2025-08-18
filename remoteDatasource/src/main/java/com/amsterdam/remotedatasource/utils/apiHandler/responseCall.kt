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
import com.amsterdam.domain.logger.LogManager
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <reified T> responseCall(
    crossinline execute: suspend () -> T,
    onHttpError: (String) -> Int? = { null },
): T {
    val tag = "responseCall"
    return try {
        val result = execute()
        LogManager.debug(tag, "Result: $result")
        result
    } catch (e: HttpException) {
        throw if (e.code() == 401) {
            val serverResponse = e.response()?.errorBody()?.string() ?: throw UnknownException()
            mapHttpCodeToDomainException(onHttpError(serverResponse), tag, e)
        } else {
            LogManager.error(tag, "HttpException", e)
            ServerErrorException()
        }
    } catch (e: ConnectException) {
        LogManager.error(tag, "ConnectException: No internet connection", e)
        throw NoInternetException()
    } catch (e: SerializationException) {
        LogManager.error(tag, "SerializationException: Serialization error", e)
        throw ServerErrorException()
    } catch (e: UnknownHostException) {
        LogManager.error(tag, "UnknownHostException: No internet ", e)
        throw NoInternetException()
    } catch (e: SocketTimeoutException) {
        LogManager.error(tag, "SocketTimeoutException: No internet", e)
        throw NetworkException()
    } catch (e: Exception) {
        LogManager.errorWithCrashlytics(tag, "Exception: Unknown error", e)
        throw NetworkException()
    }
}

fun mapHttpCodeToDomainException(
    code: Int?,
    tag: String,
    exception: HttpException
): NetworkException =
    when (code) {
        3, 14, 30 -> {
            LogManager.error(
                tag + "mapHttpCodeToDomainException",
                "HttpException: Invalid credentials",
                exception
            )
            InvalidCredentialsException()
        }

        7, 10, 16, 17, 33, 35, 36 -> {
            LogManager.error(
                tag + "mapHttpCodeToDomainException",
                "HttpException: Invalid session",
                exception
            )
            InvalidSessionException()
        }

        31 -> {
            LogManager.error(
                tag + "mapHttpCodeToDomainException",
                "HttpException: Account disabled",
                exception
            )
            AccountDisabledException()
        }

        32 -> {
            LogManager.error(
                tag + "mapHttpCodeToDomainException",
                "HttpException: Verification required",
                exception
            )
            VerificationRequiredException()
        }

        38, 39 -> {
            LogManager.error(
                tag + "mapHttpCodeToDomainException",
                "HttpException: Access denied",
                exception
            )
            AccessDeniedException()
        }

        45 -> {
            LogManager.error(
                tag + "mapHttpCodeToDomainException",
                "HttpException: Access restricted",
                exception
            )
            AccessRestrictedException()
        }

        else -> {
            LogManager.errorWithCrashlytics(
                tag + "mapHttpCodeToDomainException",
                "HttpException: Unknown error",
                exception
            )
            NetworkException()
        }
    }
