package com.amsterdam.repository.datasource.local

import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto

interface ProfileLocalDataSource {
    suspend fun getAccountDetails(): AccountDetailsLocalDto?
    suspend fun addAccountDetails(accountDetails: AccountDetailsLocalDto)
    suspend fun deleteAccountDetails()
}