package com.amsterdam.repository.dto.local.profile

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(tableName = DatabaseConstants.ACCOUNT_DETAILS_TABLE)
data class AccountDetailsLocalDto(
    @PrimaryKey val accountId: Int = 0,
    val username: String = "",
    val avatarUrl: String = "",
    val totalGamePoints: Int = 0
)
