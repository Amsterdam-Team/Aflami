package com.amsterdam.localdatasource.roomDataBase.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Dao
interface AccountDetailsDao {
    @Upsert
    suspend fun upsertAccountDetails(accountDetails: AccountDetailsLocalDto)

    @Query(
        """
        SELECT *
        FROM ${DatabaseConstants.ACCOUNT_DETAILS_TABLE}

    """
    )
    suspend fun getAccountDetails(): AccountDetailsLocalDto?

    @Query("DELETE FROM ${DatabaseConstants.ACCOUNT_DETAILS_TABLE}")
    suspend fun deleteAccountDetails()

}