package com.amsterdam.localdatasource.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.amsterdam.domain.exceptions.UnauthorizedException
import com.amsterdam.localdatasource.dataStore.AuthenticationLocalDataSourceImpl.PreferenceKeys.SESSION_ID
import com.amsterdam.localdatasource.dataStore.AuthenticationLocalDataSourceImpl.PreferenceKeys.SESSION_TYPE
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthenticationLocalDataSourceImpl @Inject constructor(
    private val datastore: DataStore<Preferences>,
) : AuthenticationLocalSource {
    private object PreferenceKeys {
        val SESSION_TYPE = stringPreferencesKey("sessionType")
        val SESSION_ID = stringPreferencesKey("session_id")
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

    override suspend fun cacheSessionId(sessionId: String) {
        datastore.edit { setting ->
            setting[SESSION_ID] = sessionId
        }
    }

    override suspend fun getCachedSessionId(): String {
        return datastore.data
            .map { setting ->
                setting[SESSION_ID] ?: throw UnauthorizedException()
            }.firstOrNull()
            ?: throw UnauthorizedException()
    }

    override suspend fun clearCachedSessionId() {
        datastore.edit { setting ->
            setting[SESSION_ID] = ""
        }
    }
}
