package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences
) : AppPreferencesRepository {
    override suspend fun getCurrentLanguage(): String = preferences.getCurrentLanguage()

    override suspend fun setCurrentLanguage(language: String) =
        preferences.setCurrentLanguage(language)
}