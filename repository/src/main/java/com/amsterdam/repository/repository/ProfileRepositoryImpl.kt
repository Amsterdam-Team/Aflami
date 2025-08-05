package com.amsterdam.repository.repository

import com.amsterdam.domain.repository.AuthenticationRepository
import com.amsterdam.domain.repository.ProfileRepository
import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.datasource.remote.ProfileDataSource
import com.amsterdam.repository.mapper.remote.toEntity
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val authenticationRepository: AuthenticationRepository
) : ProfileRepository {
    override suspend fun getAccountDetails(): AccountDetails {
        return authenticationRepository.getSessionId().let { sessionId ->
            profileDataSource.getAccountDetails(sessionId = sessionId).toEntity()
        }
    }
}