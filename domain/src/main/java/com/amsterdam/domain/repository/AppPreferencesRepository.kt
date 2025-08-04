package com.amsterdam.domain.repository

import com.amsterdam.domain.utils.RestrictionLevel
import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {
    fun getDeviceLanguage(): Flow<String>
    suspend fun setDeviceLanguage(language: String)
    suspend fun setOnboardingCompleted(isCompleted: Boolean)
    suspend fun isOnboardingCompleted(): Boolean

    fun getRestrictionLevel(): Flow<RestrictionLevel>
    suspend fun setRestrictionLevel(restrictionLevel: RestrictionLevel)
}