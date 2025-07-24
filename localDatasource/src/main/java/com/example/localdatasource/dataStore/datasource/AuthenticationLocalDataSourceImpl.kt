package com.example.localdatasource.dataStore.datasource

import com.example.repository.datasource.local.AuthenticationLocalSource

class AuthenticationLocalDataSourceImpl(
    private val appPreferences: AppPreferences
): AuthenticationLocalSource {
    override suspend fun setSessionType(sessionType: String) {
        appPreferences.setSessionType(sessionType)
    }

    override suspend fun getSessionType(): String {
        return appPreferences.getSessionType()
    }
}