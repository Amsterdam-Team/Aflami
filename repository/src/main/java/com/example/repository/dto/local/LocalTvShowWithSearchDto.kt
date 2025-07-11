package com.example.repository.dto.local

import androidx.room.Entity

@Entity(
    tableName = "tvshow_search",
    primaryKeys = ["tvShowId", "searchKeyword"]
)
data class LocalTvShowWithSearchDto(
    val tvShowId: Long,
    val searchKeyword: String
)
