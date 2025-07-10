package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.relation.SearchType

@Entity(tableName = "search")
data class SearchDto(
    val searchKeyword: String,
    val searchType: SearchType,
    val movieId: Long
)

