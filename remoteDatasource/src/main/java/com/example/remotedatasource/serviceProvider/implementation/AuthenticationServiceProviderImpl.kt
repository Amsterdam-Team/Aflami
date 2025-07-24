package com.example.remotedatasource.serviceProvider.implementation

import com.example.remotedatasource.api.AuthenticationApiService
import com.example.remotedatasource.serviceProvider.AuthenticationServiceProvider
import com.example.remotedatasource.utils.apiHandler.responseCall
import com.example.repository.dto.remote.authentication.AuthenticationResponseDto
import com.example.repository.dto.remote.authentication.CreateSessionDto
import kotlinx.serialization.json.Json

class AuthenticationServiceProviderImpl(
    private val json: Json,
    private val authenticationApiService: AuthenticationApiService,
) : AuthenticationServiceProvider {
    override suspend fun createRequestToken(): AuthenticationResponseDto {
        val response =
            responseCall({ authenticationApiService.createRequestToken() }) {
                val response = json.decodeFromString<AuthenticationResponseDto>(it)
                response.statusCode!!
            }

        return response
    }

    override suspend fun createSessionWithLogin(
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

    override suspend fun createSession(requestToken: String) =
        responseCall({ authenticationApiService.createSession(requestToken) }) {
            val response = json.decodeFromString<AuthenticationResponseDto>(it)
            response.statusCode!!
        }
}
