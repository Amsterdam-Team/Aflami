package com.amsterdam.remotedatasource.datasource

import android.util.Log
import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.dto.remote.authentication.AuthenticationResponseDto
import com.amsterdam.repository.dto.remote.authentication.CreateSessionDto
import com.amsterdam.repository.dto.remote.authentication.DeleteSessionDto
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val json: Json,
    private val authenticationApiService: AuthenticationApiService
) : AuthenticationRemoteSource {
    override suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String {
        val unactiveToken = createRequestToken()
        val activeToken =
            createSessionWithLogin(username, password, unactiveToken.requestToken ?: "")
        return createSession(activeToken.requestToken ?: "").sessionId
    }

    override suspend fun deleteSession(sessionId: String): Boolean {
        return responseCall(
            { authenticationApiService.deleteSession(DeleteSessionDto(sessionId)) }
        ) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }.isSuccess
    }

    private suspend fun createRequestToken(): AuthenticationResponseDto {
        val response =
            responseCall({ authenticationApiService.createRequestToken() }) {
                val response = json.decodeFromString<AuthenticationResponseDto>(it)
                response.statusCode!!
            }

        return response
    }

    private suspend fun createSessionWithLogin(
        username: String,
        password: String,
        requestToken: String,
    ): AuthenticationResponseDto {
        val response =
            responseCall({
                authenticationApiService.createSessionWithLogin(
                    CreateSessionDto(
                        username = username,
                        password = password,
                        requestToken = requestToken,
                    ),
                )
            }) {
                val response = json.decodeFromString<AuthenticationResponseDto>(it)
                response.statusCode!!
            }

        return response
    }

    private suspend fun createSession(requestToken: String) =
        responseCall({ authenticationApiService.createSession(requestToken) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
}
