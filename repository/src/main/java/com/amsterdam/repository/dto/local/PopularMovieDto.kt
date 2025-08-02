package com.amsterdam.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import com.amsterdam.repository.dto.local.utils.DatabaseConstants
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = DatabaseConstants.POPULAR_MOVIE_TABLE,
    primaryKeys = ["movieId", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["movieId", "storedLanguage"],
            childColumns = ["movieId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
)
data class PopularMovieDto(
    val movieId: Long,
    val storedLanguage: String,
    val dateAdded: Instant = Clock.System.now()
)
