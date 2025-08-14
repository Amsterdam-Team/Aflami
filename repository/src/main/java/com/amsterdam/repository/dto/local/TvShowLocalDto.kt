package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.LocalDate

@Entity(
    tableName = DatabaseConstants.TV_SHOW_TABLE,
    primaryKeys = ["tvShowId", "storedLanguage"],
    indices = [
        Index(value = ["tvShowId", "storedLanguage"], unique = true),
    ]
)
data class TvShowLocalDto(
    val tvShowId: Long,
    val storedLanguage: String,
    val name: String,
    val description: String,
    val poster: String,
    val airDate: LocalDate?,
    val rating: Float,
    val popularity: Double,
    val seasonCount: Int,
    val originCountry: String
)