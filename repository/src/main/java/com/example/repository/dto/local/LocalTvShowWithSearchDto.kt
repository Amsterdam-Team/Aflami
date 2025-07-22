package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TV_SHOW_SEARCH_TABLE,
    primaryKeys = ["tvShowId", "searchKeyword", "storedLanguage"]
)
data class LocalTvShowWithSearchDto(
    val tvShowId: Long,
    val searchKeyword: String,
    val storedLanguage: String,
)
