package com.amsterdam.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
    suspend fun isOnboardingCompleted(): Boolean

    fun getRestrictionLevel(): Flow<String>
    suspend fun setRestrictionLevel(restrictionLevel: String)

    suspend fun setAppLanguage(language: String)
    fun getAppLanguage(): Flow<String>

    suspend fun setAppTheme(isDarkTheme: Boolean)
    fun getAppTheme(): Flow<Boolean>
}