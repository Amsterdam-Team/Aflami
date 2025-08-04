package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.mapper.local.SessionTypeMapper
import com.amsterdam.repository.security.CryptoData
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationRemoteSource: AuthenticationRemoteSource,
    private val authenticationLocalSource: AuthenticationLocalSource,
    private val sessionTypeMapper: SessionTypeMapper,
    val cryptoData: CryptoData,
) : AuthenticationRepository {
    override suspend fun loginWithPassword(
        username: String,
        password: String,
    ) {
        authenticationRemoteSource.loginWithPassword(username, password).let { sessionId ->
            authenticationLocalSource.cacheSessionId(cryptoData.encryptString(sessionId))
        }
        authenticationLocalSource.setSessionType(sessionTypeMapper.toLocalSessionType(SessionType.LOGGED_IN))
    }

    override suspend fun getSessionId(): String =
        cryptoData.decryptString(authenticationLocalSource.getCachedSessionId()) ?: throw UnknownException()

    override suspend fun setSessionType(sessionType: SessionType) {
        authenticationLocalSource.setSessionType(sessionTypeMapper.toLocalSessionType(sessionType))
    }

    override suspend fun getSessionType(): SessionType =
        sessionTypeMapper.fromLocalSessionType(authenticationLocalSource.getSessionType())

    override suspend fun logout() {
        val sessionId = getSessionId()
        if (authenticationRemoteSource.deleteSession(sessionId)){
            authenticationLocalSource.clearCachedSessionId()
            authenticationLocalSource.setSessionType(sessionTypeMapper.toLocalSessionType(SessionType.NOT_LOGGED_IN))
        }
    }
}
