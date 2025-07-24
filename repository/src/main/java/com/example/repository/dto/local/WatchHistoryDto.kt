package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Entity(
    tableName = DatabaseConstants.WATCH_HISTORY_TABLE,
)
data class WatchHistoryDto(
    @PrimaryKey val movieId: Long,
    val storedLanguage: String,
    val lastWatchedTime: Instant
)
