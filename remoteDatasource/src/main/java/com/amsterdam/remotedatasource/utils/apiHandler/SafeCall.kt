package com.amsterdam.remotedatasource.utils.apiHandler

import android.util.Log.e
import com.amsterdam.domain.exceptions.AflamiException
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): T {
    val response = try {
            execute()
        } catch (e: Exception) {
        e("bk", "safeCall: $e")
        throw AflamiException()
        }

    return responseToResult(response)
}