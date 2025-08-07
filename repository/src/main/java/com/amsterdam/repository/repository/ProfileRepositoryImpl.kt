package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.datasource.remote.ProfileRemoteDataSource
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto
import com.amsterdam.repository.mapper.local.toEntity
import com.amsterdam.repository.mapper.remote.toEntity
import com.amsterdam.repository.mapper.remoteToLocal.toLocal
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val authenticationRepository: AuthenticationRepository
) : ProfileRepository {
    override suspend fun getAccountDetails(): AccountDetails {
        return authenticationRepository.getSessionId()
            .takeIf { it.isNotBlank() }
            ?.let { sessionId ->
                getAccountDetailsFromLocal()?.takeIf { accountDetails ->
                    accountDetails.username.isNotBlank()
                }?.toEntity() ?: getAccountDetailsFromRemote(sessionId).toEntity()
            } ?: AccountDetails(accountId = 0, username = "", avatarUrl = "")
    }

    private suspend fun getAccountDetailsFromLocal(): AccountDetailsLocalDto? {
        return profileLocalDataSource.getAccountDetails()
    }

    private suspend fun getAccountDetailsFromRemote(sessionId: String): AccountDetailsRemoteDto {
        return profileRemoteDataSource.getAccountDetails(sessionId = sessionId)
            .let { accountDetails ->
                profileLocalDataSource.addAccountDetails(accountDetails.toLocal())
                accountDetails
            }
    }
}