package com.example.remotedatasource.utils.apiHandler

import android.util.Log
import com.example.domain.exceptions.NoInternetException
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): T {
    val response = try {
            execute()
        } catch (e: Exception) {
        Log.e("bk", "safeCall: $e")
        throw NoInternetException()
        }

    return responseToResult(response)
}