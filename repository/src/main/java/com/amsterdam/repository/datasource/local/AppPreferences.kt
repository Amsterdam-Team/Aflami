package com.amsterdam.repository.datasource.local

interface AppPreferences {
    suspend fun getDeviceLanguage(): String
    suspend fun setDeviceLanguage(language: String)
}