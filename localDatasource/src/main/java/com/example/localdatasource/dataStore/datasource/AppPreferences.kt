package com.example.localdatasource.dataStore.datasource

interface AppPreferences {
    suspend fun getSessionType(): String
    suspend fun setSessionType(sessionType: String)
}