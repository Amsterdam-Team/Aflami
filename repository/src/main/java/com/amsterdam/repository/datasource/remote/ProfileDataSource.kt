package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.profile.AccountDetailsDto

interface ProfileDataSource {
    suspend fun getAccountDetails(sessionId: String): AccountDetailsDto
}