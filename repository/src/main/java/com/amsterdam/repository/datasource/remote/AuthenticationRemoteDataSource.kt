package com.amsterdam.repository.datasource.remote

interface AuthenticationRemoteDataSource {
    suspend fun loginWithPassword(
        username: String,
        password: String,
    ): String
}
