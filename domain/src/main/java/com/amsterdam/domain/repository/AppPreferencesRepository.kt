package com.amsterdam.domain.repository

interface AppPreferencesRepository {
    suspend fun getDeviceLanguage(): String
    suspend fun setDeviceLanguage(language: String)
}