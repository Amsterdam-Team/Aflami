package com.example.repository.datasource.remote

interface AuthenticationRemoteSource {
    suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String
}
