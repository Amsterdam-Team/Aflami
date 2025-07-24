package com.example.repository.datasource.local

interface AuthenticationLocalSource {
    suspend fun setSessionType(sessionType: String)
    suspend fun getSessionType(): String
}