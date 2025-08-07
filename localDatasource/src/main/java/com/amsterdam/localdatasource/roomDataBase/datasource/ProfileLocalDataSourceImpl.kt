package com.amsterdam.localdatasource.roomDataBase.datasource

import com.amsterdam.localdatasource.roomDataBase.daos.AccountDetailsDao
import com.amsterdam.repository.datasource.local.ProfileLocalDataSource
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import javax.inject.Inject

class ProfileLocalDataSourceImpl @Inject constructor(
    private val accountDetailsDao: AccountDetailsDao
) : ProfileLocalDataSource {
    override suspend fun getAccountDetails(): AccountDetailsLocalDto? {
        return accountDetailsDao.getAccountDetails()
    }

    override suspend fun addAccountDetails(accountDetails: AccountDetailsLocalDto) {
        accountDetailsDao.upsertAccountDetails(accountDetails)
    }

    override suspend fun deleteAccountDetails() {
        accountDetailsDao.deleteAccountDetails()
    }

}