package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.SearchType
import kotlinx.datetime.Instant

@Entity(
    tableName = "SearchDto",
    primaryKeys = ["searchKeyword", "searchType"]
)
data class LocalSearchDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val expireDate: Instant
)
