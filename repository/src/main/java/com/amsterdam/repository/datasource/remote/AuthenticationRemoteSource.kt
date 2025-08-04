package com.amsterdam.repository.datasource.remote

interface AuthenticationRemoteSource {
    suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String

    suspend fun deleteSession(sessionId: String): Boolean
}
