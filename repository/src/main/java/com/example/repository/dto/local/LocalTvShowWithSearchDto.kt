package com.example.repository.dto.local

import androidx.room.Entity
import com.example.repository.dto.local.utils.DatabaseContract

@Entity(
    tableName = DatabaseContract.TV_SHOW_SEARCH_TABLE,
    primaryKeys = ["tvShowId", "searchKeyword"]
)
data class LocalTvShowWithSearchDto(
    val tvShowId: Long,
    val searchKeyword: String
)
