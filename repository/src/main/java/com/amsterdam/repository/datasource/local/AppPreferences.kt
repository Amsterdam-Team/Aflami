package com.amsterdam.repository.datasource.local

import kotlinx.coroutines.flow.Flow

interface AppPreferences {
    fun getDeviceLanguage(): Flow<String>
    suspend fun setDeviceLanguage(language: String)
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
    suspend fun isOnboardingCompleted(): Boolean

    fun getRestrictionLevel(): Flow<String>
    suspend fun setRestrictionLevel(restrictionLevel: String)
}