package com.amsterdam.localdatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.amsterdam.localdatasource.dataStore.AppLocalPreferencesDataStore.PreferenceKeys.CURRENT_LANGUAGE
import com.amsterdam.localdatasource.dataStore.AppLocalPreferencesDataStore.PreferenceKeys.IS_DARK_THEME
import com.amsterdam.localdatasource.dataStore.AppLocalPreferencesDataStore.PreferenceKeys.IS_ONBOARDING_COMPLETED
import com.amsterdam.localdatasource.dataStore.AppLocalPreferencesDataStore.PreferenceKeys.RESTRICTION_LEVEL
import com.amsterdam.repository.datasource.local.AppLocalPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppLocalPreferencesDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppLocalPreferences {

    override suspend fun setAppLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[CURRENT_LANGUAGE] = language
        }
    }

    override fun getAppLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CURRENT_LANGUAGE] ?: ""
        }
    }

    override suspend fun setAppTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME] = isDarkTheme
        }
    }

    override fun getAppTheme(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME] ?: true
        }
    }

    override fun getRestrictionLevel(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[RESTRICTION_LEVEL] ?: "STRICT"
        }
    }

    override suspend fun setRestrictionLevel(restrictionLevel: String) {
        dataStore.edit { preferences ->
            preferences[RESTRICTION_LEVEL] = restrictionLevel
        }
    }

    override suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] = isCompleted
        }
    }

    override suspend fun isOnboardingCompleted(): Boolean {
        return dataStore.data.first()[IS_ONBOARDING_COMPLETED] ?: false
    }

    private object PreferenceKeys {
        val CURRENT_LANGUAGE = stringPreferencesKey("current_language")
        val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
        val RESTRICTION_LEVEL = stringPreferencesKey("restriction_level")
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
    }
}