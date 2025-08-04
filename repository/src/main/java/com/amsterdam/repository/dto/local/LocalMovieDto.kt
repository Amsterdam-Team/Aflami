package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.Index
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@Entity(
    tableName = DatabaseConstants.MOVIE_TABLE,
    primaryKeys = ["movieId", "storedLanguage"],
    indices = [
        Index(value = ["movieId", "storedLanguage"], unique = true),
    ]
)
data class LocalMovieDto(
    val movieId: Long,
    val storedLanguage: String,
    val name: String,
    val description: String,
    val poster: String,
    val releaseDate: LocalDate,
    val popularity: Double,
    val rating: Float,
    val originCountry: String,
    val movieLength: Int,
    val insertedDate : Instant = Clock.System.now()
)
