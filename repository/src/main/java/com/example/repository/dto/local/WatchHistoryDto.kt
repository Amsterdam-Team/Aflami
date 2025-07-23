package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.WATCH_HISTORY_TABLE,
    primaryKeys = ["movieId", "storedLanguage", "lastWatchedTime"],
    indices = [
        Index(value = ["movieId", "storedLanguage", "lastWatchedTime"], unique = true),
    ],
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["movieId", "storedLanguage"],
            childColumns = ["movieId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WatchHistoryDto(
    val movieId: Long,
    val storedLanguage: String,
    val lastWatchedTime: Long
)
