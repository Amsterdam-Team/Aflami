package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Entity(
    tableName = DatabaseConstants.MOVIE_WATCH_HISTORY_TABLE,
)
data class MovieWatchHistoryDto(
    @PrimaryKey val movieId: Long,
    val storedLanguage: String,
    val lastWatchedTime: Instant
)
