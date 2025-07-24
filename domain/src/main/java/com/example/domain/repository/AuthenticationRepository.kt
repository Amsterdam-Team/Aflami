package com.example.domain.repository

import com.example.domain.utils.SessionType

interface AuthenticationRepository {
    suspend fun loginWithPassword(
        username: String,
        password: String,
    )

    suspend fun setSessionType(sessionType: SessionType)
    suspend fun getSessionType(): SessionType
}
