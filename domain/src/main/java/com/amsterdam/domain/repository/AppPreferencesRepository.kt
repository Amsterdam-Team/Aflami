package com.amsterdam.domain.repository

interface AppPreferencesRepository {
    suspend fun getCurrentLanguage(): String
    suspend fun setCurrentLanguage(language: String)
}