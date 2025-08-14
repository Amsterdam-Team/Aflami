package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.AuthenticationApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteDataSource
import com.amsterdam.repository.dto.remote.authentication.AuthenticationRemoteResponse
import com.amsterdam.repository.dto.remote.authentication.CreateSessionRemoteDto
import kotlinx.serialization.json.Json
import javax.inject.Inject

class AuthenticationRemoteDataSourceImpl @Inject constructor(
    private val json: Json,
    private val authenticationApiService: AuthenticationApiService
) : AuthenticationRemoteDataSource {
    override suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String {
        val unactiveToken = createRequestToken()
        val activeToken =
            createSessionWithLogin(username, password, unactiveToken.requestToken ?: "")
        return createSession(activeToken.requestToken ?: "").sessionId
    }

    private suspend fun createRequestToken(): AuthenticationRemoteResponse {
        val response =
            responseCall({ authenticationApiService.createRequestToken() }) {
                val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
                response.statusCode!!
            }

        return response
    }

    private suspend fun createSessionWithLogin(
        username: String,
        password: String,
        requestToken: String,
    ): AuthenticationRemoteResponse {
        val response =
            responseCall({
                authenticationApiService.createSessionWithLogin(
                    CreateSessionRemoteDto(
                        username = username,
                        password = password,
                        requestToken = requestToken,
                    ),
                )
            }) {
                val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
                response.statusCode!!
            }

        return response
    }

    private suspend fun createSession(requestToken: String) =
        responseCall({ authenticationApiService.createSession(requestToken) }) {
            val response = json.decodeFromString<AuthenticationRemoteResponse>(it)
            response.statusCode!!
        }
}
