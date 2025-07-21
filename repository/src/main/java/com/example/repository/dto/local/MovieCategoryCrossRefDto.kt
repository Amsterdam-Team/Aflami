package com.example.repository.dto.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.example.repository.dto.local.utils.DatabaseConstants

@Entity(
    tableName = DatabaseConstants.MOVIE_CATEGORY_CROSS_REF_TABLE,
    primaryKeys = ["movieId", "categoryId", "storedLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = LocalMovieDto::class,
            parentColumns = ["movieId", "storedLanguage"],
            childColumns = ["movieId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalMovieCategoryDto::class,
            parentColumns = ["categoryId", "storedLanguage"],
            childColumns = ["categoryId", "storedLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["movieId", "storedLanguage"]),
        Index(value = ["categoryId", "storedLanguage"])
    ]
)
data class MovieCategoryCrossRefDto(
    val movieId: Long,
    val categoryId: Long,
    val storedLanguage: String
)
