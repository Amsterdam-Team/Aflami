package com.example.repository.dto.local

import androidx.room.Entity

@Entity(tableName = "tvshow_search")
data class LocalTvShowWithSearchDto(val tvShowId: Long, val searchKeyword: String)
