package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AppPreferencesRepository
import com.amsterdam.repository.datasource.local.AppPreferences
import javax.inject.Inject

class AppPreferencesRepositoryImpl @Inject constructor(
    private val preferences: AppPreferences
) : AppPreferencesRepository {
    override suspend fun getDeviceLanguage(): String = preferences.getDeviceLanguage()

    override suspend fun setDeviceLanguage(language: String) =
        preferences.setDeviceLanguage(language)
}