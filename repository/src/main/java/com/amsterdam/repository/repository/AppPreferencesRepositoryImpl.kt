package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.domain.utils.RestrictionLevel
import com.amsterdam.repository.datasource.local.AppPreferences
import com.amsterdam.repository.mapper.stringToRestrictionLevelEntity
import com.amsterdam.repository.mapper.toLocalDto
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences,
) : AppPreferencesRepository {
    override fun getAppLanguage(): Flow<String> = preferences.getAppLanguage()

    override suspend fun setAppLanguage(language: String) =
        preferences.setAppLanguage(language)

    override fun getAppTheme(): Flow<Boolean> {
        return preferences.getAppTheme()
    }

    override suspend fun setAppTheme(isDarkTheme: Boolean) {
        preferences.setAppTheme(isDarkTheme)
    }
    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        preferences.setOnboardingCompleted(isCompleted)
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return preferences.isOnboardingCompleted()
    }

    override fun getRestrictionLevel(): Flow<RestrictionLevel> {
        return stringToRestrictionLevelEntity(preferences.getRestrictionLevel())
    }

    override suspend fun setRestrictionLevel(restrictionLevel: RestrictionLevel) {
        preferences.setRestrictionLevel(restrictionLevel.toLocalDto())
    }
}