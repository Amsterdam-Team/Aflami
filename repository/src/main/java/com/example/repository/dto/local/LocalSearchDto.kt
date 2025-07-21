package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.DatabaseConstants
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

@Entity(
    tableName = DatabaseConstants.RECENT_SEARCH_TABLE,
    primaryKeys = ["searchKeyword", "searchType", "storedLanguage"]
)
data class LocalSearchDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val storedLanguage: String,
    val expireDate: Instant
)
