package com.example.remotedatasource.datasource

import com.example.remotedatasource.serviceProvider.AuthenticationServiceProvider
import com.example.repository.datasource.remote.AuthenticationRemoteSource

class AuthenticationRemoteDataSourceImpl(
    private val authenticationServiceProvider: AuthenticationServiceProvider,
) : AuthenticationRemoteSource {
    override suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String {
        val unactiveToken = authenticationServiceProvider.createRequestToken()
        val activeToken =
            authenticationServiceProvider.createSessionWithLogin(username, password, unactiveToken.requestToken ?: "")
        return authenticationServiceProvider.createSession(activeToken.requestToken ?: "").sessionId
    }
}
