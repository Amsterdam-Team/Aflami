package com.amsterdam.repository.repository

import com.amsterdam.domain.exceptions.UnknownException
import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.utils.SessionType
import com.amsterdam.repository.datasource.local.AuthenticationLocalDataSource
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.AuthenticationRemoteDataSource
import com.amsterdam.repository.mapper.stringToSessionTypeEntity
import com.amsterdam.repository.mapper.toLocalDto
import com.amsterdam.repository.security.CryptoManager
import com.amsterdam.repository.utils.decryptString
import com.amsterdam.repository.utils.encryptString
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource,
    val cryptoManager: CryptoManager,
) : AuthenticationRepository {
    override suspend fun loginWithPassword(username: String, password: String) {
        authenticationRemoteDataSource.loginWithPassword(username, password).let { sessionId ->
            authenticationLocalDataSource.cacheSessionId(cryptoManager.encryptString(sessionId))
        }
        authenticationLocalDataSource.setSessionType(SessionType.LOGGED_IN.toLocalDto())
    }

    override suspend fun getSessionId(): String {
        return cryptoManager.decryptString(authenticationLocalDataSource.getCachedSessionId())
            ?: throw UnknownException()
    }

    override suspend fun setSessionType(sessionType: SessionType) {
        authenticationLocalDataSource.setSessionType(sessionType.toLocalDto())
    }

    override suspend fun getSessionType(): SessionType {
        return stringToSessionTypeEntity(authenticationLocalDataSource.getSessionType())
    }

    override suspend fun logout() {
        authenticationLocalDataSource.clearCachedSessionId()
        authenticationLocalDataSource.setSessionType(SessionType.NOT_LOGGED_IN.toLocalDto())
        profileLocalDataSource.deleteAccountDetails()
    }
}
