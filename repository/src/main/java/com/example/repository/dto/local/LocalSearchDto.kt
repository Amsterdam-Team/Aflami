package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.DatabaseContract
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

@Entity(
    tableName = DatabaseContract.RECENT_SEARCH_TABLE,
    primaryKeys = ["searchKeyword", "searchType"]
)
data class LocalSearchDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val expireDate: Instant
)
