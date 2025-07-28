package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Instant

@Entity(
    tableName = DatabaseConstants.TV_WATCH_HISTORY_TABLE,
)
data class TvShowWatchHistoryDto(
    @PrimaryKey val tvShowId: Long,
    val storedLanguage: String,
    val lastWatchedTime: Instant
)