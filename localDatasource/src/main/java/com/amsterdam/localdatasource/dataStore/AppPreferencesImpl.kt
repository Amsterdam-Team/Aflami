package com.amsterdam.localdatasource.dataStore

import android.os.LocaleList
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.amsterdam.localdatasource.dataStore.AppPreferencesImpl.PreferenceKeys.CURRENT_LANGUAGE
import com.amsterdam.repository.datasource.local.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferencesImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {

    override suspend fun setDeviceLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[CURRENT_LANGUAGE] = language
        }
    }

    override fun getDeviceLanguage(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CURRENT_LANGUAGE] ?: LocaleList.getDefault()[0].language.lowercase()
        }
    }

    private object PreferenceKeys {
        val CURRENT_LANGUAGE = stringPreferencesKey("current_language")
    }
}