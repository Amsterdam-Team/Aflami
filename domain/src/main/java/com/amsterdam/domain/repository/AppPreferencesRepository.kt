package com.amsterdam.domain.repository

import com.amsterdam.domain.utils.RestrictionLevel
import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
    suspend fun isOnboardingCompleted(): Boolean

    fun getRestrictionLevel(): Flow<RestrictionLevel>
    suspend fun setRestrictionLevel(restrictionLevel: RestrictionLevel)

    suspend fun setAppLanguage(language: String)
    fun getAppLanguage(): Flow<String>

    suspend fun setAppTheme(isDarkTheme: Boolean)
    fun getAppTheme(): Flow<Boolean>
}