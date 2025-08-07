package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalSource
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteSource
import com.amsterdam.repository.mapper.local.stringToSessionTypeEntity
import com.amsterdam.repository.mapper.local.toLocalDto
import com.amsterdam.repository.security.CryptoData
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationRemoteSource: AuthenticationRemoteSource,
    private val authenticationLocalSource: AuthenticationLocalSource,
    private val profileLocalDataSource: ProfileLocalDataSource,
    val cryptoData: CryptoData,
) : AuthenticationRepository {
    override suspend fun loginWithPassword(
        username: String,
        password: String,
    ) {
        authenticationRemoteSource.loginWithPassword(username, password).let { sessionId ->
            authenticationLocalSource.cacheSessionId(cryptoData.encryptString(sessionId))
        }
        authenticationLocalSource.setSessionType(SessionType.LOGGED_IN.toLocalDto())
    }

    override suspend fun getSessionId(): String =
        cryptoData.decryptString(authenticationLocalSource.getCachedSessionId())
            ?: throw UnknownException()

    override suspend fun setSessionType(sessionType: SessionType) {
        authenticationLocalSource.setSessionType(sessionType.toLocalDto())
    }

    override suspend fun getSessionType(): SessionType =
        stringToSessionTypeEntity(authenticationLocalSource.getSessionType())

    override suspend fun logout() {
        authenticationLocalSource.clearCachedSessionId()
        authenticationLocalSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto())
        profileLocalDataSource.deleteAccountDetails()
    }
}
