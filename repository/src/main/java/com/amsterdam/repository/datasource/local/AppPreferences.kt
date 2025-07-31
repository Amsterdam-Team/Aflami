package com.amsterdam.repository.datasource.local

interface AppPreferences {
    suspend fun getCurrentLanguage(): String
    suspend fun setCurrentLanguage(language: String)
}