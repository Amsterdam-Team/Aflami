package com.example.domain.repository

interface AuthenticationRepository {
    suspend fun loginWithPassword(
        username: String,
        password: String,
    )
}
