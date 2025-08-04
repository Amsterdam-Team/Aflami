package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.RECENT_SEARCH_TABLE,
)
data class LocalSearchDto(
    @PrimaryKey val searchKeyword: String
)
