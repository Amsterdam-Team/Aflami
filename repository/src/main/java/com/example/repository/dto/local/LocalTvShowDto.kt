package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.TV_SHOW_TABLE,
    primaryKeys = ["tvShowId", "storedLanguage"],
    indices = [
        Index(value = ["tvShowId", "storedLanguage"], unique = true),
    ]
)
data class LocalTvShowDto(
    val tvShowId: Long,
    val storedLanguage: String,
    val name: String,
    val description: String,
    val poster: String,
    val productionYear: Int,
    val rating: Float,
    val popularity: Double,
    val seasonCount: Int,
    val originCountry: String
)