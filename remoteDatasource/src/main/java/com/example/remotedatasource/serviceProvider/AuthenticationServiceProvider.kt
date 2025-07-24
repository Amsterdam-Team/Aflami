package com.example.remotedatasource.serviceProvider

import com.example.repository.dto.remote.authentication.AuthenticationResponseDto
import com.example.repository.dto.remote.authentication.CreateSessionResponseDto

interface AuthenticationServiceProvider {
    suspend fun createRequestToken(): AuthenticationResponseDto

    suspend fun createSessionWithLogin(
        username: String,
        password: String,
        requestToken: String,
    ): AuthenticationResponseDto

    suspend fun createSession(requestToken: String): CreateSessionResponseDto
}
