package com.amsterdam.repository.datasource.remote

import com.amsterdam.repository.dto.remote.profile.AccountDetailsRemoteDto

interface ProfileRemoteDataSource {
    suspend fun getAccountDetails(): AccountDetailsRemoteDto
}