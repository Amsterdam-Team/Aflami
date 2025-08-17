package com.amsterdam.repository.datasource.local

interface AuthenticationLocalDataSource {
    suspend fun setSessionType(sessionType: String)

    suspend fun getSessionType(): String

    suspend fun cacheSessionId(sessionId: String)

    suspend fun getCachedSessionId(): String

    suspend fun clearCachedSessionId()
}
