package com.amsterdam.remotedatasource.datasource

import com.amsterdam.remotedatasource.api.ProfileApiService
import com.amsterdam.remotedatasource.utils.apiHandler.responseCall
import com.amsterdam.repository.datasource.remote.ProfileDataSource
import com.amsterdam.repository.dto.remote.profile.AccountDetailsDto
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val profileApiService: ProfileApiService
) : ProfileDataSource {
    override suspend fun getAccountDetails(sessionId: String): AccountDetailsDto {
        return responseCall { profileApiService.getAccountDetails(sessionId = sessionId) }
    }
}