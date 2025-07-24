package com.example.localdatasource.dataStore.appPreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.localdatasource.dataStore.appPreferences.AppDataStorePreferences.PreferenceKeys.SESSION_TYPE
import com.example.localdatasource.dataStore.datasource.AppPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppDataStorePreferences(
    private val datastore: DataStore<Preferences>
): AppPreferences {
    private object PreferenceKeys {
        val SESSION_TYPE = stringPreferencesKey("sessionType")
    }

    override suspend fun getSessionType(): String {
        return datastore.data.map { settings ->
            settings[SESSION_TYPE] ?: ""
        }.first()
    }

    override suspend fun setSessionType(sessionType: String) {
        datastore.edit { settings ->
            settings[SESSION_TYPE] = sessionType
        }
    }
}